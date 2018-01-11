package poker.gui;

// Preload image
var test = javafx.scene.image.Image { url: "{__DIR__}/dorian/card-back.png" };
// Initialize GUI
var gui = GUI { prefetch: test };
gui.initialize();
