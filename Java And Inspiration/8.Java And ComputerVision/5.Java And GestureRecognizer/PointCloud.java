import java.lang.Math;
import java.util.ArrayList;

public class PointCloud
{
  public static final int NUM_POINTS = 32; 
  private String _name = null;
  private ArrayList<PointCloudPoint> _points = null;
  
  // the following is NOT part of the originally published javascript implementation
  // and has been added to support addition of directional testing for point clouds
  // which represent unistroke gestures
  private boolean _isUnistroke = true;

  PointCloud(String name, double[] X, double[] Y, int[]ID)
  {
    _points = new ArrayList<PointCloudPoint>();
    for(int i = 0; i < X.length; i++)
    {
      _points.add(new PointCloudPoint(X[i], Y[i], ID[i]));
    }
    
    _name = name;

    int id = _points.get(0).getID();
    for(int i = 1; i < _points.size(); i++)
    {
      if(_points.get(i).getID() != id)
      {
        _isUnistroke = false;
        break;
      }
    }
  }
  
  public PointCloud(String name, ArrayList<PointCloudPoint> points)
  {
    if(name == null || name == "")
    {
      throw new IllegalArgumentException("Point cloud name must be supplied");
    }

    _name = name;

    if(null == points || points.size() < 2)
    {
      throw new IllegalArgumentException("Point cloud points do not define a gesture of minimum length");
    }

    _points = points;
    _points = PointCloudUtils.resample(_points, NUM_POINTS);
    _points = PointCloudUtils.scale(_points);
    _points = PointCloudUtils.translateTo(_points, PointCloudUtils.ORIGIN);
    
    // the following is NOT part of the originally published javascript implementation
    // and has been added to support addition of directional testing for point clouds
    // which represent unistroke gestures
    int id = _points.get(0).getID();
    for(int i = 1; i < _points.size(); i++)
    {
      if(_points.get(i).getID() != id)
      {
        _isUnistroke = false;
        break;
      }
    }
  }

  // the following is NOT part of the originally published javascript implementation
  // and has been added to support addition of directional testing for point clouds
  // which represent unistroke gestures
  public boolean isUnistroke()
  {
    return _isUnistroke;
  }
  
  public PointCloudPoint getFirstPoint()
  {
	  return _points.get(0);
  }

  public PointCloudPoint getLastPoint()
  {
	  return _points.get(_points.size() - 1);
  }

  public String getName()
  {
    return _name;
  }

  ArrayList<PointCloudPoint> getPoints()
  {
    return _points;
  }
  
  double greedyMatch(PointCloud reference)
  {
    double pointCount = (double) _points.size();
    double e = 0.50;
    double step = Math.floor(Math.pow(pointCount, 1.0 - e));

    double min = Double.POSITIVE_INFINITY;

    for(double i = 0.0; i < pointCount; i += step)
    {
      double d1 = this.cloudDistance(reference, i);
      double d2 = reference.cloudDistance(this, i);
      min = Math.min(min, Math.min(d1, d2)); // min3
    }

    return min;
  }

  private double cloudDistance(PointCloud reference, double start)
  {
    ArrayList<PointCloudPoint> pts1 = _points;
    ArrayList<PointCloudPoint> pts2 = reference._points;
    
    if(pts1.size() != pts2.size())
    {
      throw new IllegalArgumentException("Both point clouds must contain the same number of points");
    }

    double pointCount = (double) pts1.size();
    boolean matched[] = new boolean[(int)pointCount];

    for(int k = 0; k < pointCount; k++)
    {
      matched[k] = false;
    }

    double sum = 0;
    double i = start;

    do
    {
      int index = -1;
      double min = Double.POSITIVE_INFINITY;

      for(int j = 0; j < matched.length; j++)
      {
        if (!matched[j])
        {
          double d = PointCloudUtils.distance(pts1.get((int)i), pts2.get(j));
          if (d < min)
          {
            min = d;
            index = j;
          }
        }
      }

      matched[index] = true;
      double weight = 1.0 - ((i - start + pointCount) % pointCount) / pointCount;
      sum += weight * min;
      i = (i + 1.0) % pointCount;
    } while (i != start);

    return sum;
  }
}
