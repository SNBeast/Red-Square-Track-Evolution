public class Node {
	private double[] inputs;
	private double output = 0;
	public Node (int inputs) {
		this.inputs = new double[inputs];
	}
	public void setInput (int index, double value) {
		inputs[index] = value;
	}
	public double getOutput () {
		return output;
	}
	public void updateOutputs () {
		double accumulator = 0;
		for (double d : inputs) accumulator += d;
		output = limit(-3, 3, accumulator)/3;
	}
	public static double limit (double min, double max, double value) {
		return Math.max(min, Math.min(value, max));
	}
}