public class PointCloudPoint
{
  private double _x = Double.NaN;
  private double _y = Double.NaN;
  private int _id = -1;

  public PointCloudPoint(double x, double y, int id)
  {
    _x = x;
    _y = y;
    _id = id;
  }

  public double getX()
  {
    return _x;
  }

  public double getY()
  {
    return _y;
  }

  public int getID()
  {
    return _id;
  }
}
