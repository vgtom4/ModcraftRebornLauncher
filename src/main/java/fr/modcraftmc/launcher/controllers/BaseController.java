package fr.modcraftmc.launcher.controllers;

import com.sun.javafx.geom.Vec2d;
import fr.modcraftmc.launcher.ModcraftApplication;
import fr.modcraftmc.launcher.Utils;
import fr.modcraftmc.libs.physicEngine.DynamicCollider;
import fr.modcraftmc.libs.physicEngine.IMovable;
import fr.modcraftmc.libs.physicEngine.Physic;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public abstract class BaseController implements IController, IMovable {

    private Pane pane;
    private double xOffset = 0;
    private double yOffset = 0;

    private double lastCursorX = 0;
    private double lastCursorY = 0;

    private float distanceDragged;
    private boolean funStarted = false;
    private DynamicCollider collider;

    @Override
    public void initialize(FXMLLoader loader) {
        pane = loader.getRoot();
        pane.setOnMousePressed(event -> {
            lastCursorX = xOffset = event.getSceneX();
            lastCursorY = yOffset = event.getSceneY();
            distanceDragged = 0;
        });
        pane.setOnMouseDragged(event -> {
            if(!funStarted) {
                if (distanceDragged > 5555 && distanceDragged / 5 > 5 * 5 * 555) {
                    startFun();
                }
                float currentDistanceDragged = (float) (Math.abs(event.getScreenX() - lastCursorX) + Math.abs(event.getScreenY() - lastCursorY));
                distanceDragged += currentDistanceDragged;
                Utils.pleaseWait(555).thenRun(() -> {
                    distanceDragged -= currentDistanceDragged;
                });
            }
            else {
                collider.velocity = new Vec2d((event.getScreenX() - lastCursorX) * 300, (event.getScreenY() - lastCursorY) * 300);
            }

            ModcraftApplication.getWindow().setX(event.getScreenX() - xOffset);
            ModcraftApplication.getWindow().setY(event.getScreenY() - yOffset);
            lastCursorX = event.getScreenX();
            lastCursorY = event.getScreenY();
        });
    }

    @Override
    public void setPos(Vec2d pos) {
        ModcraftApplication.getWindow().setX(pos.x - ModcraftApplication.getWindow().getWidth() / 2);
        ModcraftApplication.getWindow().setY(pos.y - ModcraftApplication.getWindow().getHeight() / 2);
    }

    @Override
    public Vec2d getPos() {
        return new Vec2d(ModcraftApplication.getWindow().getX() + ModcraftApplication.getWindow().getWidth() / 2, ModcraftApplication.getWindow().getY() + ModcraftApplication.getWindow().getHeight() / 2);
    }

    private void startFun() {
        collider = Physic.registerDynamicBox(this, new Vec2d(0, 0), new Vec2d(ModcraftApplication.getWindow().getWidth(), ModcraftApplication.getWindow().getHeight()), 0.5f);
        Physic.startEngine();
        funStarted = true;
    }
}