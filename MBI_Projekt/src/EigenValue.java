import Jama.Matrix;

public class EigenValue implements Comparable<EigenValue> {

	private double value;
	private double[] vector;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double[] getVector() {
		return vector;
	}

	public void setVector(Matrix m, int col) {
		this.vector = new double[m.getRowDimension()];
		for (int i = 0; i < this.vector.length; ++i)
			this.vector[i] = m.get(i, col);
	}

	public EigenValue(double v) {
		value = v;
	}

	@Override
	public int compareTo(EigenValue arg0) {

		return Double.compare(this.getValue(), arg0.getValue());
	}

}
