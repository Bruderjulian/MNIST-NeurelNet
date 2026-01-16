import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;

// A label is a text that can be displayed on the screen.
// It uses a GreenfootImage to display the text.
// The text is centered on the screen! not good :(

// Bret has already explained this in class.
public class Label extends Actor {
  private int textSize = 20;
  private Color color = Color.BLACK;

  public Label(String text, int textSize) {
    this.textSize = textSize;
    setImage(new GreenfootImage(text, textSize, Color.BLACK, new Color(0, 0, 0, 0)));
  }

  public Label(String text, int textSize, Color color) {
    this.textSize = textSize;
    this.color = color;
    setImage(new GreenfootImage(text, textSize, color, new Color(0, 0, 0, 0)));
  }

  public void set(String text) {
    setImage(new GreenfootImage(text, textSize, color, new Color(0, 0, 0, 0)));
  }
}
