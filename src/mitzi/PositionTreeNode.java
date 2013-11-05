package mitzi;

import javax.swing.tree.DefaultMutableTreeNode;

public class PositionTreeNode extends DefaultMutableTreeNode implements
		Comparable<PositionTreeNode> {

	private static final long serialVersionUID = -1981743349658620167L;

	private IMove parent_move;

	public PositionTreeNode(Position position) {
		super(position);
	}

	public PositionTreeNode(Position position, boolean allowsChildren) {
		super(position, allowsChildren);
	}

	public IMove getParentMove() {
		return parent_move;
	}

	public void setParentMove(IMove parent_move) {
		this.parent_move = parent_move;
	}

	@Override
	public Position getUserObject() {
		return (Position) userObject;
	}
	
	public Position getPosition() {
		return getUserObject();
	}

	@Override
	public int compareTo(PositionTreeNode o) {
		if (o == null)
			throw new NullPointerException();

		if (this == o)
			return 0;

		if (this.getUserObject().getAnalysisResult() == null
				^ o.getUserObject().getAnalysisResult() == null)
			return (this.getUserObject().getAnalysisResult() == null) ? -1 : 1;

		if (this.getUserObject().getAnalysisResult() == null
				&& o.getUserObject().getAnalysisResult() == null)
			return 0;

		return Integer.compare(this.getUserObject().getAnalysisResult().score,
				o.getUserObject().getAnalysisResult().score);
	}

}
