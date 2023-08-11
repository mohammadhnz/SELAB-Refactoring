package parser;

abstract class Action {
    protected int number;

    public Action(int number) {
        this.number = number;
    }

    public abstract String toString();
}

class AcceptAction extends Action {
    public AcceptAction(int number) {
        super(number);
    }

    @Override
    public String toString() {
        return "acc";
    }
}

class ShiftAction extends Action {
    public ShiftAction(int number) {
        super(number);
    }

    @Override
    public String toString() {
        return "s" + number;
    }
}

class ReduceAction extends Action {
    public ReduceAction(int number) {
        super(number);
    }

    @Override
    public String toString() {
        return "r" + number;
    }
}
