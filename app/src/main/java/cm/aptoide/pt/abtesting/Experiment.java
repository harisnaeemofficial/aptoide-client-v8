package cm.aptoide.pt.abtesting;

/**
 * Created by franciscocalado on 15/06/18.
 */

public class Experiment {
  private static final long TWENTY_FOUR_HOURS = 86400000;

  private long requestTime;
  private String assignment;
  private String payload;
  private boolean partOfExperiment;
  private boolean experimentOver;

  public Experiment(String payload, String assignment, boolean experimentOver) {
    this.requestTime = System.currentTimeMillis();
    this.assignment = assignment;
    this.payload = payload;
    this.partOfExperiment = true;
    this.experimentOver = experimentOver;
  }

  public Experiment() {
    this.requestTime = -1;
    this.assignment = "";
    this.payload = "";
    this.partOfExperiment = false;
  }

  public Experiment(long requestTime, String assignment, String payload, boolean partOfExperiment,
      boolean experimentOver) {
    this.requestTime = requestTime;
    this.assignment = assignment;
    this.payload = payload;
    this.partOfExperiment = partOfExperiment;
    this.experimentOver = experimentOver;
  }

  public long getRequestTime() {
    return requestTime;
  }

  public boolean isExpired() {
    return requestTime < (System.currentTimeMillis() - TWENTY_FOUR_HOURS);
  }

  public String getAssignment() {
    return assignment;
  }

  public String getPayload() {
    return payload;
  }

  public boolean isPartOfExperiment() {
    return partOfExperiment;
  }

  public boolean isExperimentOver() {
    return experimentOver;
  }
}
