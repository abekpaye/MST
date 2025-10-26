package model;

public class OperationCounter {
    public long operations = 0;
    public void add(long n) { operations += n; }
    public long get() { return operations; }
}
