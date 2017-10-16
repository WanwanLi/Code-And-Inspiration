public class PointCloudMatchResult
{
  private String _name = null;
  private double _score = Double.NaN;

  public PointCloudMatchResult(String name, double score) // constructor
  {
    _name = name;
    _score = score;
  }

  public String getName()
  {
    return _name;
  }

  public double getScore()
  {
    return _score;
  }
}
