package au.com.codeka.warworlds.game.solarsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import au.com.codeka.warworlds.DialogManager;
import au.com.codeka.warworlds.R;
import au.com.codeka.warworlds.ctrl.ColonyList;
import au.com.codeka.warworlds.ctrl.FleetList;
import au.com.codeka.warworlds.model.Colony;
import au.com.codeka.warworlds.model.EmpireManager;
import au.com.codeka.warworlds.model.Fleet;
import au.com.codeka.warworlds.model.MyEmpire;
import au.com.codeka.warworlds.model.ScoutReport;
import au.com.codeka.warworlds.model.SectorManager;
import au.com.codeka.warworlds.model.Star;

public class ScoutReportDialog extends Dialog implements DialogManager.DialogConfigurable {
    private static Logger log = LoggerFactory.getLogger(ScoutReportDialog.class);
    private Context mContext;
    private ReportAdapter mReportAdapter;

    public static final int ID = 98475;

    public ScoutReportDialog(Activity activity) {
        super(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.scout_report_dlg);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = LayoutParams.MATCH_PARENT;
        params.width = LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        final View progressBar = findViewById(R.id.progress_bar);
        final View reportList = findViewById(R.id.report_items);
        final View reportDate = findViewById(R.id.report_date);
        final View newerButton = findViewById(R.id.newer_btn);
        final View olderButton = findViewById(R.id.older_btn);
        final ListView reportItems = (ListView) findViewById(R.id.report_items);

        progressBar.setVisibility(View.VISIBLE);
        reportList.setVisibility(View.GONE);
        reportDate.setEnabled(false);
        newerButton.setEnabled(false);
        olderButton.setEnabled(false);

        mReportAdapter = new ReportAdapter();
        reportItems.setAdapter(mReportAdapter);
    }

    @Override
    public void setBundle(Activity activity, Bundle bundle) {
        final View progressBar = findViewById(R.id.progress_bar);
        final View reportList = findViewById(R.id.report_items);

        progressBar.setVisibility(View.VISIBLE);
        reportList.setVisibility(View.GONE);

        String starKey = bundle.getString("au.com.codeka.warworlds.StarKey");
        Star star = SectorManager.getInstance().findStar(starKey);
        if (star == null) {
            // TODO: should never happen...
            log.error("SectorManager.findStar() returned null!");
            return;
        }

        EmpireManager.getInstance().getEmpire().fetchScoutReports(star, new MyEmpire.FetchScoutReportCompleteHandler() {
            @Override
            public void onComplete(List<ScoutReport> reports) {
                progressBar.setVisibility(View.GONE);
                reportList.setVisibility(View.VISIBLE);

                refreshReports(reports);
            }
        });
    }

    private void refreshReports(List<ScoutReport> reports) {

        Spinner reportDates = (Spinner) findViewById(R.id.report_date);
        reportDates.setAdapter(new ReportDatesAdapter(reports));

        if (reports.size() > 0) {
            final View reportDate = findViewById(R.id.report_date);
            final View newerButton = findViewById(R.id.newer_btn);
            final View olderButton = findViewById(R.id.older_btn);
            reportDate.setEnabled(true);
            newerButton.setEnabled(true);
            olderButton.setEnabled(true);

            mReportAdapter.setReport(reports.get(0));
        }
    }

    private class ReportDatesAdapter extends BaseAdapter implements SpinnerAdapter {
        List<ScoutReport> mReports;

        public ReportDatesAdapter(List<ScoutReport> reports) {
            mReports = reports;
        }

        @Override
        public int getCount() {
            return mReports.size();
        }

        @Override
        public Object getItem(int position) {
            return mReports.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = getCommonView(position, convertView, parent);

            view.setTextColor(Color.WHITE);
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = getCommonView(position, convertView, parent);

            ViewGroup.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,
                                                                     LayoutParams.MATCH_PARENT);
            lp.height = 60;
            view.setLayoutParams(lp);

            view.setTextColor(Color.BLACK);
            return view;
        }

        private TextView getCommonView(int position, View convertView, ViewGroup parent) {
            TextView view;
            if (convertView != null) {
                view = (TextView) convertView;
            } else {
                view = new TextView(mContext);
                view.setGravity(Gravity.CENTER_VERTICAL);
            }

            ScoutReport report = mReports.get(position);
            DateTime now = DateTime.now(DateTimeZone.UTC);
            if (now.compareTo(report.getReportDate()) < 0) {
                view.setText("0 hrs ago");
            } else {
                Interval interval = new Interval(report.getReportDate(), now);
                Duration duration = interval.toDuration();
                view.setText(String.format("%d hrs ago", duration.toStandardHours().getHours()));
            }
            return view;
        }
    }

    private class ReportAdapter extends BaseAdapter {
        private List<ReportItem> mItems;
        private Star mStar;

        public ReportAdapter() {
            mItems = new ArrayList<ReportItem>();
        }

        public void setReport(ScoutReport report) {
            mItems.clear();
            mStar = report.getStarSnapshot();

            Star star = report.getStarSnapshot();
            for (Colony colony : star.getColonies()) {
                ReportItem item = new ReportItem();
                item.colony = colony;
                mItems.add(item);
            }

            for (Fleet fleet : star.getFleets()) {
                ReportItem item = new ReportItem();
                item.fleet = fleet;
                mItems.add(item);
            }

            Collections.sort(mItems, new Comparator<ReportItem>() {
                @Override
                public int compare(ReportItem lhs, ReportItem rhs) {
                    if (lhs.colony != null && rhs.colony == null) {
                        return -1;
                    } else if (lhs.fleet != null && rhs.fleet == null) {
                        return 1;
                    }

                    if (lhs.colony != null) {
                        final Colony lhsColony = lhs.colony;
                        final Colony rhsColony = rhs.colony;

                        return lhsColony.getPlanetIndex() - rhsColony.getPlanetIndex();
                    } else {
                        final Fleet lhsFleet = lhs.fleet;
                        final Fleet rhsFleet = rhs.fleet;

                        if (lhsFleet.getDesignID().equals(rhsFleet.getDesignID())) {
                            return lhsFleet.getNumShips() - rhsFleet.getNumShips();
                        } else {
                            // TODO: can we trust this sort? so far we can...
                            return lhsFleet.getDesignID().compareTo(rhsFleet.getDesignID());
                        }
                    }
                }
            });

            notifyDataSetChanged();
        }

        /**
         * Two kinds of views, one for the colonies and one for the fleets.
         */
        @Override
        public int getViewTypeCount() {
            return 2;
        }

        /**
         * We just check whether it's a colony or fleet in that position.
         */
        @Override
        public int getItemViewType(int position) {
            if (mItems.get(position).colony != null) {
                return 0;
            } else {
                return 1;
            }
        }

        /**
         * None of the items in the list are selectable.
         */
        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ReportItem item = mItems.get(position);
            View view = convertView;

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                if (item.colony != null) {
                    view = inflater.inflate(R.layout.colony_list_row, null);
                } else {
                    view = inflater.inflate(R.layout.fleet_list_row, null);
                }
            }

            if (item.colony != null) {
                Colony colony = item.colony;

                ColonyList.populateColonyListRow(mContext, view, colony, mStar);
                TextView uncollectedTaxes = (TextView) view.findViewById(R.id.colony_taxes);
                uncollectedTaxes.setText("");
            } else {
                Fleet fleet = item.fleet;
                FleetList.populateFleetRow(mContext, null, view, fleet);
            }

            return view;

        }

        private class ReportItem {
            public Colony colony;
            public Fleet fleet;
        }
    }
}
