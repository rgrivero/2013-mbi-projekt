package pca;

import Jama.Matrix;

/**
 * 
 *
 */
public class EigenValue implements Comparable<EigenValue> {

	private double value;
	private double[] vector;

	/**
	 * @return
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @return
	 */
	public double[] getVector() {
		return vector;
	}

	/**
	 * @param m
	 * @param col
	 */
	public void setVector(Matrix m, int col) {
		this.vector = new double[m.getRowDimension()];
		for (int i = 0; i < this.vector.length; ++i)
			this.vector[i] = m.get(i, col);
	}

	/**
	 * @param v
	 */
	public EigenValue(double v) {
		value = v;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(EigenValue arg0) {

		return Double.compare(this.getValue(), arg0.getValue());
	}
}
