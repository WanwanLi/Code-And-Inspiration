#include <QString>
#include "QStanMath.h"
using namespace std;

class QEnergy
{
	private:
	int sketchWidth=0;
	int sketchHeight=0;
	const int jointDim=5;
	const int curveEnd=-1;
	enum 
	{
		PARALLEL=1, 
		COLLINEAR=2, 
		PERPENDICULAR,
		FORESHORTENING,
		GROUND_PARALLEL,
		GROUND_COLLINEAR,
		GROUND_PERPENDICULAR
	};
	const Vector4v viewDirection=Vector4v(0, 0, -1, 0);
	const Vector4v groundPlane=Vector4v(0, 1, 0, 0);
	int getJointType(QString s);
	QStanMath<var> stanMath;

	public:
	VectorXd planeVector;
	VectorXi sketchVector, jointVector;
	bool loadSketchFile(QString fileName);
	var totalEnergy(const VectorXv& plane);
	MatrixXd sketchPlanes(VectorXd plane);
	Vector3v sketchPoint(Vector3i point, const MatrixXv& planes);
	var parallelEnergy(const Vector4v& plane1, const Vector4v& plane2);
	MatrixXv sketchCurves(const MatrixXi& sketch, const MatrixXv& planes);
	var perpendicularEnergy(const Vector4v& plane1, const Vector4v& plane2);
	var collinearEnergy(const MatrixXv& curve, const Vector4v& plane, int start, int end);
};