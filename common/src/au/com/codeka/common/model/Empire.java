// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ./messages.proto
package au.com.codeka.common.model;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoEnum;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.ENUM;
import static com.squareup.wire.Message.Datatype.FLOAT;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Label.REPEATED;
import static com.squareup.wire.Message.Label.REQUIRED;

public final class Empire extends Message {

  public static final String DEFAULT_KEY = "";
  public static final String DEFAULT_DISPLAY_NAME = "";
  public static final String DEFAULT_USER = "";
  public static final String DEFAULT_EMAIL = "";
  public static final EmpireState DEFAULT_STATE = EmpireState.INITIAL;
  public static final List<Fleet> DEFAULT_FLEETS = Collections.emptyList();
  public static final List<Colony> DEFAULT_COLONIES = Collections.emptyList();
  public static final List<BuildRequest> DEFAULT_BUILD_REQUESTS = Collections.emptyList();
  public static final Float DEFAULT_CASH = 0F;
  public static final EmpireRank DEFAULT_RANK = getDefaultInstance(EmpireRank.class);
  public static final Star DEFAULT_HOME_STAR = getDefaultInstance(Star.class);
  public static final Alliance DEFAULT_ALLIANCE = getDefaultInstance(Alliance.class);

  /**
   * A unique identifier for the empire, useful for referencing him in other parts of the
   * system. This won't be present in the initial "PUT"
   */
  @ProtoField(tag = 1, type = STRING)
  public String key;

  /**
   * The "display" name for the player, which is what other players see.
   */
  @ProtoField(tag = 2, type = STRING, label = REQUIRED)
  public String display_name;

  /**
   * The player's Google Account identifier (which is actually unique of their email address)
   */
  @ProtoField(tag = 3, type = STRING)
  public String user;

  /**
   * The player's email address
   */
  @ProtoField(tag = 4, type = STRING)
  public String email;

  @ProtoField(tag = 5, type = ENUM, label = REQUIRED)
  public EmpireState state;

  /**
   * May not be set, but if it is this is all of the fleets owned by this empire
   */
  @ProtoField(tag = 6, label = REPEATED)
  public List<Fleet> fleets;

  /**
   * May not be set, but if it si this is all of the colonies owned by the empire
   */
  @ProtoField(tag = 7, label = REPEATED)
  public List<Colony> colonies;

  /**
   * May not be set, but if it is then this will be all the build requests currently in progress
   */
  @ProtoField(tag = 10, label = REPEATED)
  public List<BuildRequest> build_requests;

  /**
   * If this is set, then it will be a list of the stars referenced by the fleets,
   * colonies and build requests. This is useful because you often want details of the stars the
   * colony/fleet is on (so you can draw the icon, etc). Note that it will *only*
   * be the stars and the planets, not colonies, build requests, etc.
   * repeated Star stars = 8;
   * the amount of cash the empire currently has (todo: provide an estimate on the
   * rate of change of cash, based on taxed income)
   */
  @ProtoField(tag = 9, type = FLOAT)
  public Float cash;

  /**
   * if specified, contains the details of the empire's rank
   */
  @ProtoField(tag = 11)
  public EmpireRank rank;

  /**
   * if specified, this is the empire's "home star", which is the star we should centre on when
   * we're asked to view the empire. It's usually the start that contains the HQ or, if they don't
   * have one, basically just a random star they control.
   */
  @ProtoField(tag = 12)
  public Star home_star;

  /**
   * if this empire is part of an alliance, this will contain the details of the alliance.
   */
  @ProtoField(tag = 13)
  public Alliance alliance;

  private Empire(Builder builder) {
    super(builder);
    this.key = builder.key;
    this.display_name = builder.display_name;
    this.user = builder.user;
    this.email = builder.email;
    this.state = builder.state;
    this.fleets = immutableCopyOf(builder.fleets);
    this.colonies = immutableCopyOf(builder.colonies);
    this.build_requests = immutableCopyOf(builder.build_requests);
    this.cash = builder.cash;
    this.rank = builder.rank;
    this.home_star = builder.home_star;
    this.alliance = builder.alliance;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Empire)) return false;
    Empire o = (Empire) other;
    return equals(key, o.key)
        && equals(display_name, o.display_name)
        && equals(user, o.user)
        && equals(email, o.email)
        && equals(state, o.state)
        && equals(fleets, o.fleets)
        && equals(colonies, o.colonies)
        && equals(build_requests, o.build_requests)
        && equals(cash, o.cash)
        && equals(rank, o.rank)
        && equals(home_star, o.home_star)
        && equals(alliance, o.alliance);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = key != null ? key.hashCode() : 0;
      result = result * 37 + (display_name != null ? display_name.hashCode() : 0);
      result = result * 37 + (user != null ? user.hashCode() : 0);
      result = result * 37 + (email != null ? email.hashCode() : 0);
      result = result * 37 + (state != null ? state.hashCode() : 0);
      result = result * 37 + (fleets != null ? fleets.hashCode() : 0);
      result = result * 37 + (colonies != null ? colonies.hashCode() : 0);
      result = result * 37 + (build_requests != null ? build_requests.hashCode() : 0);
      result = result * 37 + (cash != null ? cash.hashCode() : 0);
      result = result * 37 + (rank != null ? rank.hashCode() : 0);
      result = result * 37 + (home_star != null ? home_star.hashCode() : 0);
      result = result * 37 + (alliance != null ? alliance.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<Empire> {

    public String key;
    public String display_name;
    public String user;
    public String email;
    public EmpireState state;
    public List<Fleet> fleets;
    public List<Colony> colonies;
    public List<BuildRequest> build_requests;
    public Float cash;
    public EmpireRank rank;
    public Star home_star;
    public Alliance alliance;

    public Builder() {
    }

    public Builder(Empire message) {
      super(message);
      if (message == null) return;
      this.key = message.key;
      this.display_name = message.display_name;
      this.user = message.user;
      this.email = message.email;
      this.state = message.state;
      this.fleets = copyOf(message.fleets);
      this.colonies = copyOf(message.colonies);
      this.build_requests = copyOf(message.build_requests);
      this.cash = message.cash;
      this.rank = message.rank;
      this.home_star = message.home_star;
      this.alliance = message.alliance;
    }

    public Builder key(String key) {
      this.key = key;
      return this;
    }

    public Builder display_name(String display_name) {
      this.display_name = display_name;
      return this;
    }

    public Builder user(String user) {
      this.user = user;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder state(EmpireState state) {
      this.state = state;
      return this;
    }

    public Builder fleets(List<Fleet> fleets) {
      this.fleets = fleets;
      return this;
    }

    public Builder colonies(List<Colony> colonies) {
      this.colonies = colonies;
      return this;
    }

    public Builder build_requests(List<BuildRequest> build_requests) {
      this.build_requests = build_requests;
      return this;
    }

    public Builder cash(Float cash) {
      this.cash = cash;
      return this;
    }

    public Builder rank(EmpireRank rank) {
      this.rank = rank;
      return this;
    }

    public Builder home_star(Star home_star) {
      this.home_star = home_star;
      return this;
    }

    public Builder alliance(Alliance alliance) {
      this.alliance = alliance;
      return this;
    }

    @Override
    public Empire build() {
      checkRequiredFields();
      return new Empire(this);
    }
  }

  public enum EmpireState {
    @ProtoEnum(1)
    INITIAL,
    @ProtoEnum(2)
    REGISTERED,
    @ProtoEnum(3)
    BANNED,
  }
}