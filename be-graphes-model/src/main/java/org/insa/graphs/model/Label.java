package org.insa.graphs.model;

public class Label implements Comparable<Label>{
	public Node currentPeak;
	
	private boolean marked;
	
	private double cost;
	
	private Arc father;
	
	public double getCost() {
		return this.cost;
	}
	
	public Label(Node currentPeak, Arc father,double cost,boolean marked) {
		this.cost = cost;
		this.currentPeak = currentPeak;
		this.father = father;
		this.marked = marked;
	}
	
	public void setCost(double newCost) {
		this.cost = newCost;
	}
	
	public void setFather(Arc newFather) {
		this.father = newFather;
	}
	
	public Arc getFather() {
		return this.father;
	}
	
	public void setMarked() {
		this.marked = true;
	}
	
	public boolean isMarked() {
		return this.marked;
	}
	
	public double getCoutTotal() {
		return this.cost;
	}
	
	public int compareTo(Label other) {
        return Double.compare(this.getCoutTotal(), other.getCoutTotal());
    }
}