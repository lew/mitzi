package mitzi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HashManager {

	private PositionTreeNode root_node;

	private Map<IBoard, PositionTreeNode> hash_map = new HashMap<IBoard, PositionTreeNode>();

	HashManager(Position position) {
		root_node = new PositionTreeNode(position);
		hash_map.put(position.board, root_node);
	}

	public void rebase(PositionTreeNode new_root) {
		new_root.removeFromParent();
		new_root.setParentMove(null);
		root_node = new_root;
		clearHashMap();
	}

	public PositionTreeNode lookup(Position position) {
		PositionTreeNode ptn = hash_map.get(position.board);

		// check for cache hit
		if (ptn != null) {
			return ptn;
		}

		// register new PTN
		ptn = new PositionTreeNode(position);
		hash_map.put(position.board, ptn);

		return ptn;
	}

	public PositionTreeNode getRootNode() {
		return root_node;
	}

	public void clearHashMap() {
		if (hash_map.isEmpty())
			return;

		int cutoff = 6;

		Iterator<Map.Entry<IBoard, PositionTreeNode>> it = hash_map.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<IBoard, PositionTreeNode> entry = it.next();
			it.remove();

			if (entry.getValue() == root_node)
				continue;

			if (!root_node.isNodeDescendant(entry.getValue())) {
				hash_map.remove(entry.getKey());
			} else if (entry.getValue().getPosition().getAnalysisResult() == null) {
				hash_map.remove(entry.getKey());
				entry.getValue().removeFromParent();
			} else if (cutoff < root_node.getDepth()
					&& entry.getValue().getPosition().getAnalysisResult()
							.getPlysToSelDepth() < cutoff) {
				hash_map.remove(entry.getKey());
				entry.getValue().removeFromParent();
			}
		}
	}
}
