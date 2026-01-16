import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

// Represents a button that can be clicked and triggers an action.
// The action can be defined by the actionId parameter 
// (i am too lazy to write it better)
public class Button extends Actor {

  // action when pressed and displayed text
  private String actionId;
  private Label label;

  public Button(int sizeX, int sizeY, String text, String actionId, int textSize) {
    this.actionId = actionId;
    GreenfootImage image = new GreenfootImage(sizeX, sizeY);
    image.setColor(Color.LIGHT_GRAY);
    image.fillRect(0, 0, sizeX, sizeY);
    label = new Label(text, textSize);
    image.drawImage(label.getImage(), getCenter(image.getWidth(), label.getImage().getWidth()),
        getCenter(image.getHeight(), label.getImage().getHeight()));
    setImage(image);
  }

  // check if the button or the label is pressed, then trigger the correct action
  // by simply comparing the actionId to all possible values
  public void act() {
    if (Greenfoot.mousePressed(this) || Greenfoot.mousePressed(label)) {
      if (actionId == "nextImage") {
        ((MyWorld) getWorld()).updateNetwork();
      } else if (actionId == "showFailedImage") {
        ((MyWorld) getWorld()).showFailedImage();
      } else if (actionId == "clearImage") {
        ((MyWorld) getWorld()).clear();
      } else if (actionId == "estimateFailed") {
        ((MyWorld) getWorld()).estimateFailed();
      } else if (actionId == "train") {
        ((MyWorld) getWorld()).train();
      }
    }
  }

  // get the center of the image (used for the label)
  private static int getCenter(int v1, int v2) {
    return v1 / 2 - v2 / 2;
  }
}
