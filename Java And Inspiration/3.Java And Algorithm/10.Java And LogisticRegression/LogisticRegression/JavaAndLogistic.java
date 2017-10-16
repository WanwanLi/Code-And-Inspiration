import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.*;

public class JavaAndLogistic
{
	public static void main(String... args) throws FileNotFoundException
	{
		List<Instance> instances = DataSet.readDataSet("dataset.txt");
		Logistic logistic = new Logistic(5);
		logistic.train(instances);
		int[] x = {2, 1, 1, 0, 1};
		System.out.println("prob(1|x) = " + logistic.predict(x));
		int[] x2 = {1, 0, 1, 0, 0};
		System.out.println("prob(1|x2) = " + logistic.predict(x2));
	}
}
class Logistic
{
	private double rate;
	private double[] weights;
	private int times= 1000;

	public Logistic(int n)
	{
		this.rate = 0.0001;
		this.weights = new double[n];
	}

	private double sigmoid(double z)
	{
		return 1 / (1 + Math.exp(-z));
	}
	public double predict(int[] x)
	{
		double logit = 0.0;
		for (int i=0; i<weights.length;i++)
		{
			logit += weights[i] * x[i];
		}
		return sigmoid(logit);
	}
	private void optimize(double trueValue, double predictedValue, int[] x)
	{
		for (int j=0; j<weights.length; j++)
		{
			weights[j] = weights[j] + rate * (trueValue  - predictedValue) * x[j];
		}
	}
	public void train(List<Instance> instances)
	{

			double logLikelihood = 0.0;
			for (int i=0; i<instances.size(); i++)
			{
				int[] x = instances.get(i).getX();
				double label = 0.0+instances.get(i).getLabel();
				for (int t=0; t<times; t++)
				{
				double predicted = this.predict(x);
				this.optimize(label, predicted, x);
				}
				//logLikelihood += label* Math.log(predicted) + (1-label) * Math.log(1-predicted);
			}
			//System.out.println("iteration: " + t + " " + Arrays.toString(weights) + " log(likelihood)= : " + logLikelihood);

	}
}
