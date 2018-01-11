package poker.gui;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

public class Notification extends CustomNode {
    var kind : INotification;
    var content : Node[] = bind kind.content;
    var group = Group { content: bind content };
    
    public function show(message : String) : Void {
        kind.show(message);
    }
    override function create(): Node {
        return group;
    }
}
