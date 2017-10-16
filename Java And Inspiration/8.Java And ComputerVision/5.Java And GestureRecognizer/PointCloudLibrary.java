import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class PointCloudLibrary
{
  private static double CLOSE_ENOUGH = 0.3;
  private static PointCloudLibrary demoLibrary = null;
  private ArrayList<PointCloud> _pointClouds =  new ArrayList<PointCloud>();

  public PointCloudLibrary()
  {
  }

  public PointCloudLibrary(ArrayList<PointCloud> pointClouds)
  {
    if(null == pointClouds)
    {
      throw new IllegalArgumentException("Point clouds cannot be null");
    }
    
    _pointClouds = pointClouds;
  }

  // the following is NOT part of the originally published javascript implementation
  // and has been added to support addition of directional testing for point clouds
  // which represent unistroke gestures
  public boolean containsOnlyUnistrokes()
  {
    for(int i = 0; i < _pointClouds.size(); i++)
    {
      if(!_pointClouds.get(i).isUnistroke())
      {
        return false;
      }
    }
    
    return true;
  }

  Set<PointCloud> getPointCloud(String name)
  {
    HashSet<PointCloud> result = new HashSet<PointCloud>();
    
    for(int i = 0; i < _pointClouds.size(); i++)
    {
      if(_pointClouds.get(i).getName().equals(name))
      {
        result.add(_pointClouds.get(i));
      }
    }
    
    return result;
  }
  
  public Set<String> getNames()
  {
    HashSet<String> result = new HashSet<String>();
    for(int i = 0; i < _pointClouds.size(); i++)
    {
      result.add(_pointClouds.get(i).getName());
    }
    
    return result;
  }
  
  public void addPointCloud(PointCloud cloud)
  {
    _pointClouds.add(cloud);
  }
  
  // removes one or more point clouds carrying the specified name (which
  // is case sensitive) from the library. if no matches are found, null is
  // returned, else those removed are returned
  public ArrayList<PointCloud> removePointCloud(String name)
  {
    ArrayList<PointCloud> result = null;
    
    if(null == name || "" == name)
    {
      throw new IllegalArgumentException("Name must be provided");
    }
    
    for(int i = 0; i < _pointClouds.size(); i++)
    {
      PointCloud p = _pointClouds.get(i);
      if(name != p.getName())
      {
        continue;
      }
      
      if(result == null)
      {
        result = new ArrayList<PointCloud>();
        result.add(p);
        _pointClouds.remove(i);
      }
    }
    
    return result;
  }
  
  public void clear()
  {
    if(this == demoLibrary)
    {
      _pointClouds = new ArrayList<PointCloud>();
      populateDemoLibrary(this);
    }
    else
    {
      _pointClouds = new ArrayList<PointCloud>();
    }
  }
  
  public int getSize()
  {
    return _pointClouds.size();
  }

  // most closely matches published javascript implementation of $P,
  // returns only the single, best match. note that the score member of
  // the result contains the aggregate distances between the two gestures.
  // for the original implementation see originalRecognize() below.
  public PointCloudMatchResult recognize(PointCloud inputGesture)
  {
    return recognize(inputGesture, false);
  }

  // as with published javascript implementation of $P, this returns only
  // the single, best match. however it permits use of directional testing
  // and, as such, should be used only with unistrokes. note that the score
  // member of the result contains the aggregate distances between the two
  // gestures. for the original implementation see originalRecognize() below.
  public PointCloudMatchResult recognize(PointCloud inputGesture, boolean testDirectionality)
  {
    return recognizeAll(inputGesture, testDirectionality)[0];
  }

  // unlike the published javascript implementation of $P, this returns an array
  // of results - one for each point cloud in the library sorted in order of
  // increasing aggregate distance between the point clouds. it also permits use of
  // some simple directional testing which should be used only with unistrokes. note
  // that the score member of the result contains the aggregate distance between the
  // two gestures. for the original implementation see originalRecognize() below.
  public PointCloudMatchResult[] recognizeAll(PointCloud inputGesture, boolean testDirectionality)
  {
    // the following is NOT part of the originally published javascript implementation
    // and has been added to support addition of directional testing for point clouds
    // which represent unistroke gestures
    if(testDirectionality)
    {
      if(!inputGesture.isUnistroke())
      {
        throw new IllegalArgumentException("If testDirectionality is true, input gesture must contain a unistroke");
      }

      if(! containsOnlyUnistrokes())
      {
        throw new IllegalArgumentException("If testDirectionality is true, the point cloud library must contain only unistroke point clouds");
      }
    }

    double b = Double.POSITIVE_INFINITY;
    int u = -1;
    PointCloudMatchResult[] results = new PointCloudMatchResult[_pointClouds.size()];

    for(int i = 0; i < _pointClouds.size(); i++) // for each point-cloud template
    {
      PointCloud pointCloud = _pointClouds.get(i);

      // the following is NOT part of the originally published javascript implementation
      // and has been added to support addition of directional testing for point clouds
      // which represent unistroke gestures
      if(testDirectionality)
      {
        // test to see if the gestures match roughly in directionality
        // if not, keep looking
        PointCloudPoint refStart = pointCloud.getFirstPoint();
        PointCloudPoint inStart = inputGesture.getFirstPoint();
        PointCloudPoint refEnd = pointCloud.getLastPoint();
        PointCloudPoint inEnd = inputGesture.getLastPoint();
        
        if((PointCloudUtils.distance(refStart, inStart) > CLOSE_ENOUGH) ||
           (PointCloudUtils.distance(refEnd, inEnd) > CLOSE_ENOUGH))
        {
          results[i] = new PointCloudMatchResult(pointCloud.getName(), Double.POSITIVE_INFINITY);
          continue;
        }
      }

      double d = pointCloud.greedyMatch(inputGesture);
      results[i] = new PointCloudMatchResult(pointCloud.getName(), d);
    }

    Arrays.sort(results, new Comparator<PointCloudMatchResult>()
    {
       public int compare(PointCloudMatchResult obj1, PointCloudMatchResult obj2)
        {
          if(obj1.getScore() < obj2.getScore())
          {
            return -1;
          }
  
          if(obj1.getScore() > obj2.getScore())
          {
            return 1;
          }
  
          return 0;
        }
    });

    return results;
  }
  
  /* this method implements the recognize routine as originally published in
   * javascript. in this author's experience:
  
   * (a) the score being normalized between 0 and 1 offered little meaning or
   * relationship to the quality of the match as implied by the word "score".
   * "correct" matches were found to result with scores at both extremes of the
   * range.
   * 
   * (b) on occasion it was helpful to have the results of matches for each of
   * the point clouds in the library. the original implementation provides only
   * the "best" match.
   * 
   * (c) $P as published implements directional invariance and does so by design.
   * the main rationale for this decision stems from the value of directional invariance
   * when the recognizer is used with multi-stroke gestures. however, this attribute
   * of the recognizer renders it incapable of discerning similar but semantically
   * different unistroke gestures such as a single top->bottom or left->right
   * from a single bottom->top or right->left. 
   */
  public PointCloudMatchResult originalRecognize(PointCloud inputGesture)
  {
    double b = Double.POSITIVE_INFINITY;
    int u = -1;

    for(int i = 0; i < _pointClouds.size(); i++) // for each point-cloud template
    {
      PointCloud pointCloud = _pointClouds.get(i);
      double d = inputGesture.greedyMatch(pointCloud);
      
      if (d < b)
      {
        b = d; // best (least) distance
        u = i; // point-cloud
      }
    }

    if(u == -1)
    {
      return new PointCloudMatchResult("No match", 0.0);
    }
    else
    {
      double r = Math.max((b - 2.0) / -2.0, 0.0);
      return new PointCloudMatchResult(_pointClouds.get(u).getName(), r);
    }
  }
  
  public static PointCloudLibrary getDemoLibrary()
  {
    if(null != demoLibrary)
    {
      return demoLibrary;
    }

    demoLibrary = new PointCloudLibrary();
    populateDemoLibrary(demoLibrary);
    return demoLibrary;
  }
  
  private static void populateDemoLibrary(PointCloudLibrary library)
  {
    ArrayList<PointCloudPoint> points = new ArrayList<PointCloudPoint>();

    points.add(new PointCloudPoint(30,7,1));
    points.add(new PointCloudPoint(103,7,1));
    points.add(new PointCloudPoint(66,7,2));
    points.add(new PointCloudPoint(66,87,2));
    library.addPointCloud(new PointCloud("T", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(177,92,1));
    points.add(new PointCloudPoint(177,2,1));
    points.add(new PointCloudPoint(182,1,2));
    points.add(new PointCloudPoint(246,95,2));
    points.add(new PointCloudPoint(247,87,3));
    points.add(new PointCloudPoint(247,1,3));
    library.addPointCloud(new PointCloud("N", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(345,9,1));
    points.add(new PointCloudPoint(345,87,1));
    points.add(new PointCloudPoint(351,8,2));
    points.add(new PointCloudPoint(363,8,2));
    points.add(new PointCloudPoint(372,9,2));
    points.add(new PointCloudPoint(380,11,2));
    points.add(new PointCloudPoint(386,14,2));
    points.add(new PointCloudPoint(391,17,2));
    points.add(new PointCloudPoint(394,22,2));
    points.add(new PointCloudPoint(397,28,2));
    points.add(new PointCloudPoint(399,34,2));
    points.add(new PointCloudPoint(400,42,2));
    points.add(new PointCloudPoint(400,50,2));
    points.add(new PointCloudPoint(400,56,2));
    points.add(new PointCloudPoint(399,61,2));
    points.add(new PointCloudPoint(397,66,2));
    points.add(new PointCloudPoint(394,70,2));
    points.add(new PointCloudPoint(391,74,2));
    points.add(new PointCloudPoint(386,78,2));
    points.add(new PointCloudPoint(382,81,2));
    points.add(new PointCloudPoint(377,83,2));
    points.add(new PointCloudPoint(372,85,2));
    points.add(new PointCloudPoint(367,87,2));
    points.add(new PointCloudPoint(360,87,2));
    points.add(new PointCloudPoint(355,88,2));
    points.add(new PointCloudPoint(349,87,2));
    library.addPointCloud(new PointCloud("D", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(507,8,1));
    points.add(new PointCloudPoint(507,87,1));
    points.add(new PointCloudPoint(513,7,2));
    points.add(new PointCloudPoint(528,7,2));
    points.add(new PointCloudPoint(537,8,2));
    points.add(new PointCloudPoint(544,10,2));
    points.add(new PointCloudPoint(550,12,2));
    points.add(new PointCloudPoint(555,15,2));
    points.add(new PointCloudPoint(558,18,2));
    points.add(new PointCloudPoint(560,22,2));
    points.add(new PointCloudPoint(561,27,2));
    points.add(new PointCloudPoint(562,33,2));
    points.add(new PointCloudPoint(561,37,2));
    points.add(new PointCloudPoint(559,42,2));
    points.add(new PointCloudPoint(556,45,2));
    points.add(new PointCloudPoint(550,48,2));
    points.add(new PointCloudPoint(544,51,2));
    points.add(new PointCloudPoint(538,53,2));
    points.add(new PointCloudPoint(532,54,2));
    points.add(new PointCloudPoint(525,55,2));
    points.add(new PointCloudPoint(519,55,2));
    points.add(new PointCloudPoint(513,55,2));
    points.add(new PointCloudPoint(510,55,2));
    library.addPointCloud(new PointCloud("P", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(30,146,1));
    points.add(new PointCloudPoint(106,222,1));
    points.add(new PointCloudPoint(30,225,2));
    points.add(new PointCloudPoint(106,146,2));
    library.addPointCloud(new PointCloud("X", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(188,137,1));
    points.add(new PointCloudPoint(188,225,1));
    points.add(new PointCloudPoint(188,180,2));
    points.add(new PointCloudPoint(241,180,2));
    points.add(new PointCloudPoint(241,137,3));
    points.add(new PointCloudPoint(241,225,3));
    library.addPointCloud(new PointCloud("H", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(371,149,1));
    points.add(new PointCloudPoint(371,221,1));
    points.add(new PointCloudPoint(341,149,2));
    points.add(new PointCloudPoint(401,149,2));
    points.add(new PointCloudPoint(341,221,3));
    points.add(new PointCloudPoint(401,221,3));
    library.addPointCloud(new PointCloud("I", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(526,142,1));
    points.add(new PointCloudPoint(526,204,1));
    points.add(new PointCloudPoint(526,221,2));
    library.addPointCloud(new PointCloud("exclamation", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(12,347,1));
    points.add(new PointCloudPoint(119,347,1));
    library.addPointCloud(new PointCloud("line", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(177,396,1));
    points.add(new PointCloudPoint(223,299,1));
    points.add(new PointCloudPoint(262,396,1));
    points.add(new PointCloudPoint(168,332,1));
    points.add(new PointCloudPoint(278,332,1));
    points.add(new PointCloudPoint(184,397,1));
    library.addPointCloud(new PointCloud("five-point star", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(382,310,1));
    points.add(new PointCloudPoint(377,308,1));
    points.add(new PointCloudPoint(373,307,1));
    points.add(new PointCloudPoint(366,307,1));
    points.add(new PointCloudPoint(360,310,1));
    points.add(new PointCloudPoint(356,313,1));
    points.add(new PointCloudPoint(353,316,1));
    points.add(new PointCloudPoint(349,321,1));
    points.add(new PointCloudPoint(347,326,1));
    points.add(new PointCloudPoint(344,331,1));
    points.add(new PointCloudPoint(342,337,1));
    points.add(new PointCloudPoint(341,343,1));
    points.add(new PointCloudPoint(341,350,1));
    points.add(new PointCloudPoint(341,358,1));
    points.add(new PointCloudPoint(342,362,1));
    points.add(new PointCloudPoint(344,366,1));
    points.add(new PointCloudPoint(347,370,1));
    points.add(new PointCloudPoint(351,374,1));
    points.add(new PointCloudPoint(356,379,1));
    points.add(new PointCloudPoint(361,382,1));
    points.add(new PointCloudPoint(368,385,1));
    points.add(new PointCloudPoint(374,387,1));
    points.add(new PointCloudPoint(381,387,1));
    points.add(new PointCloudPoint(390,387,1));
    points.add(new PointCloudPoint(397,385,1));
    points.add(new PointCloudPoint(404,382,1));
    points.add(new PointCloudPoint(408,378,1));
    points.add(new PointCloudPoint(412,373,1));
    points.add(new PointCloudPoint(416,367,1));
    points.add(new PointCloudPoint(418,361,1));
    points.add(new PointCloudPoint(419,353,1));
    points.add(new PointCloudPoint(418,346,1));
    points.add(new PointCloudPoint(417,341,1));
    points.add(new PointCloudPoint(416,336,1));
    points.add(new PointCloudPoint(413,331,1));
    points.add(new PointCloudPoint(410,326,1));
    points.add(new PointCloudPoint(404,320,1));
    points.add(new PointCloudPoint(400,317,1));
    points.add(new PointCloudPoint(393,313,1));
    points.add(new PointCloudPoint(392,312,1));
    points.add(new PointCloudPoint(418,309,2));
    points.add(new PointCloudPoint(337,390,2));
    library.addPointCloud(new PointCloud("null", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(506,349,1));
    points.add(new PointCloudPoint(574,349,1));
    points.add(new PointCloudPoint(525,306,2));
    points.add(new PointCloudPoint(584,349,2));
    points.add(new PointCloudPoint(525,388,2));
    library.addPointCloud(new PointCloud("arrowhead", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(38,470,1));
    points.add(new PointCloudPoint(36,476,1));
    points.add(new PointCloudPoint(36,482,1));
    points.add(new PointCloudPoint(37,489,1));
    points.add(new PointCloudPoint(39,496,1));
    points.add(new PointCloudPoint(42,500,1));
    points.add(new PointCloudPoint(46,503,1));
    points.add(new PointCloudPoint(50,507,1));
    points.add(new PointCloudPoint(56,509,1));
    points.add(new PointCloudPoint(63,509,1));
    points.add(new PointCloudPoint(70,508,1));
    points.add(new PointCloudPoint(75,506,1));
    points.add(new PointCloudPoint(79,503,1));
    points.add(new PointCloudPoint(82,499,1));
    points.add(new PointCloudPoint(85,493,1));
    points.add(new PointCloudPoint(87,487,1));
    points.add(new PointCloudPoint(88,480,1));
    points.add(new PointCloudPoint(88,474,1));
    points.add(new PointCloudPoint(87,468,1));
    points.add(new PointCloudPoint(62,464,2));
    points.add(new PointCloudPoint(62,571,2));
    library.addPointCloud(new PointCloud("pitchfork", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(177,554,1));
    points.add(new PointCloudPoint(223,476,1));
    points.add(new PointCloudPoint(268,554,1));
    points.add(new PointCloudPoint(183,554,1));
    points.add(new PointCloudPoint(177,490,2));
    points.add(new PointCloudPoint(223,568,2));
    points.add(new PointCloudPoint(268,490,2));
    points.add(new PointCloudPoint(183,490,2));
    library.addPointCloud(new PointCloud("six-point star", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(325,499,1));
    points.add(new PointCloudPoint(417,557,1));
    points.add(new PointCloudPoint(417,499,2));
    points.add(new PointCloudPoint(325,557,2));
    points.add(new PointCloudPoint(371,486,3));
    points.add(new PointCloudPoint(371,571,3));
    library.addPointCloud(new PointCloud("asterisk", points));

    points = new ArrayList<PointCloudPoint>();
    points.add(new PointCloudPoint(546,465,1));
    points.add(new PointCloudPoint(546,531,1));
    points.add(new PointCloudPoint(540,530,2));
    points.add(new PointCloudPoint(536,529,2));
    points.add(new PointCloudPoint(533,528,2));
    points.add(new PointCloudPoint(529,529,2));
    points.add(new PointCloudPoint(524,530,2));
    points.add(new PointCloudPoint(520,532,2));
    points.add(new PointCloudPoint(515,535,2));
    points.add(new PointCloudPoint(511,539,2));
    points.add(new PointCloudPoint(508,545,2));
    points.add(new PointCloudPoint(506,548,2));
    points.add(new PointCloudPoint(506,554,2));
    points.add(new PointCloudPoint(509,558,2));
    points.add(new PointCloudPoint(512,561,2));
    points.add(new PointCloudPoint(517,564,2));
    points.add(new PointCloudPoint(521,564,2));
    points.add(new PointCloudPoint(527,563,2));
    points.add(new PointCloudPoint(531,560,2));
    points.add(new PointCloudPoint(535,557,2));
    points.add(new PointCloudPoint(538,553,2));
    points.add(new PointCloudPoint(542,548,2));
    points.add(new PointCloudPoint(544,544,2));
    points.add(new PointCloudPoint(546,540,2));
    points.add(new PointCloudPoint(546,536,2));
    library.addPointCloud(new PointCloud("half-note", points));
  }
}
