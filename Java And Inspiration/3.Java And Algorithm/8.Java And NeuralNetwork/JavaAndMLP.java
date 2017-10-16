import java.util.Arrays;
import java.util.Random;
class MLPLayer
{
	int row,col;
	float[] output;
	float[] input;
	float[][] weights;
	float[][] dweights;
	boolean isSigmoid = true;
	public MLPLayer(int inputSize, int outputSize, Random r)
	{
		row=outputSize;
		col=inputSize+1;
		input=new float[col];
		output=new float[row];
		weights=new float[row][col];
		dweights=new float[row][col];
		this.initWeights(r);
	}
	public void initWeights(Random r)
	{
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
	 			weights[i][j]=(r.nextFloat()-0.5f)*4f;
			}
		}
	}
	public float[] run(float[] in)
	{
		System.arraycopy(in, 0, input, 0, in.length);
		input[input.length - 1] = 1;
		Arrays.fill(output, 0);
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				output[i] += weights[i][j]*input[j];
			}
			if (isSigmoid)
			{
				output[i] = (float) (1 / (1 + Math.exp(-output[i])));
			}
		}
		return Arrays.copyOf(output, output.length);
	}
	public float[] train(float[] error, float learningRate, float momentum)
	{
		float[] nextError = new float[col];
		for(int i=0;i<row;i++)
		{
			float d = error[i];
			if (isSigmoid)
			{
				d *= output[i] * (1 - output[i]);
			}
			for(int j=0;j<col;j++)
			{
				nextError[j] += weights[i][j] * d;
				float dw = input[j] * d * learningRate;
				weights[i][j] += dweights[i][j] * momentum + dw;
				dweights[i][j] = dw;
			}
		}
		return nextError;
	}
}
class MLP 
{
	MLPLayer[] layers;
	public MLP(int inputSize, int[] layersSize)
	{
		layers = new MLPLayer[layersSize.length];
		Random r = new Random(1234);
		for (int i = 0; i < layersSize.length; i++)
		{
			int inSize = i == 0 ? inputSize : layersSize[i - 1];
			layers[i] = new MLPLayer(inSize, layersSize[i], r);	
		}
	}
	public float[] run(float[] input)
	{
		float[] actIn = input;
		for (int i = 0; i < layers.length; i++)
		{
			actIn = layers[i].run(actIn);
		}
		return actIn;
	}
	public void train(float[] input, float[] targetOutput, float learningRate, float momentum) 
	{
		float[] calcOut = run(input);
		float[] error = new float[calcOut.length];
		for (int i = 0; i < error.length; i++) 
		{
			error[i] = targetOutput[i] - calcOut[i]; // negative error
		}
		for (int i = layers.length - 1; i >= 0; i--) 
		{
			error = layers[i].train(error, learningRate, momentum);
		}
	}
}
class JavaAndMLP
{
	public static void main(String[] args)
	{
		float[][] train=new float[][]
		{
			new float[]{0, 0},
			new float[]{0, 1},
			new float[]{1, 0},
			new float[]{1, 1}
		};
		float[][] res=new float[][]
		{
			new float[]{0}, 
			new float[]{1}, 
			new float[]{1}, 
			new float[]{0}
		};
		MLP mlp = new MLP(2,new int[]{2,1});
		mlp.layers[1].isSigmoid=false;
		Random r= new Random();
		int en = 500;
		for (int e = 0; e < en; e++)
		{
			for (int i = 0; i < res.length; i++)
			{
				int idx = r.nextInt(res.length);
				mlp.train(train[idx], res[idx], 0.3f, 0.6f);
			}
			if ((e + 1) % 100 == 0)
			{
				System.out.println();
				for (int i = 0; i < res.length; i++) 
				{
					float[] t = train[i];
					System.out.printf("%d epoch\n", e + 1);
					System.out.printf("%.1f, %.1f --> %.3f\n", t[0], t[1], mlp.run(t)[0]);
				}
			}
		}
	}
}


