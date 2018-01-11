package poker.gui;

import javafx.scene.Node;

public abstract class INotification {
    public var content : Node[];
    public abstract function show(message : String): Void;
    public abstract function initialize(): Void;
}