package frame;

import action.EditAction;
import action.LayerDeletionAction;
import action.PasteAction;
import action.PixelChange;
import tool.BaseTool;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {

    static private boolean isPrimarySelected = true;
    static public Color primaryColor;
    static public Color secondaryColor;

    public static void setIsPrimarySelected(boolean b) {
        isPrimarySelected = b;
    }

    public static Color selectedColor() {
        if(isPrimarySelected) return primaryColor;
        else return secondaryColor;
    }

    public static void setSelectedColor(Color color) {
        Frame.colorSettings.setSelectedColor(color);
    }

    private static Canvas activeCanvas;

    public static void addNewCanvas(Canvas canvas) {
        Frame.topPanel.createTabForCanvas(canvas);
        setActiveCanvas(canvas);
    }

    public static void addNewLayerToActiveCanvas(int index) {
        Layer newLayer = new Layer(activeCanvas);
        newLayer.setName("Layer " + activeCanvas.nextNameNumber());
        activeCanvas.addLayer(newLayer, index);
        Frame.layerPanel.addLayer(newLayer, index);
    }

    public static void restoreLayerToActiveCanvas(Layer layer, int index) {
        activeCanvas.addLayer(layer, index);
        Frame.layerPanel.addLayer(layer, index);
    }

    public static void mergeLayerIntoLayer(Layer topLayer, Layer bottomLayer, int bottomIndex) {
        bottomLayer.mergeLayerIntoThis(topLayer);

        deleteLayerFromActiveCanvas(bottomIndex - 1);
    }

    public static void undoMergeLayer(Layer topLayer, Layer bottomLayer, int bottomIndex) {
        int topIndex = bottomIndex - 1;
        restoreLayerToActiveCanvas(bottomLayer, bottomIndex);
        restoreLayerToActiveCanvas(topLayer, topIndex);
        deleteLayerFromActiveCanvas(bottomIndex);
    }

    public static void deleteLayerFromActiveCanvas(int index) {
        activeCanvas.deleteLayer(index);
        Frame.layerPanel.deleteLayer(index);
    }

    public static void addLayerToActiveCanvas(Layer layer, int index) {
        activeCanvas.addLayer(layer, index);
        Frame.layerPanel.addLayer(layer, index);
    }

    public static void setActiveCanvas(Canvas canvas) {
        activeCanvas = canvas;
        Frame.onCanvasSwap(canvas);

        LayerPanelHandler.setActiveCanvas(canvas);
        AnimationPanel.setCanvas(canvas);
        BaseTool.canvas = canvas;
    }

    public static Canvas getActiveCanvas() {
        return activeCanvas;
    }

    public static void undo() {
        try {
            activeCanvas.undoManager.undo();
        } catch (Exception e) {
            NotificationPanel.playNotification(NotificationMessage.NO_MORE_UNDO);
        }
    }

    public static void redo() {
        try {
            activeCanvas.undoManager.redo();
        } catch (Exception e) {
            NotificationPanel.playNotification(NotificationMessage.NO_MORE_REDO);
        }
    }

    public static void copy() {
        if(activeCanvas == null) {
            NotificationPanel.playNotification(NotificationMessage.NO_CANVAS);
            return;
        }
        if(activeCanvas.getSelectedArea() == null) {
            TransferableImage transferableImage = new TransferableImage(activeCanvas.getActiveLayer().getImage());

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(transferableImage, null);
        } else {
            Rectangle selectedArea = activeCanvas.getSelectedArea();
            BufferedImage selectedPart = activeCanvas.getActiveLayer().getImage().getSubimage(selectedArea.x, selectedArea.y, selectedArea.width, selectedArea.height);
            TransferableImage transferableImage = new TransferableImage(selectedPart);

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(transferableImage, null);
        }
    }

    public static void cut() {
        if(activeCanvas == null) {
            NotificationPanel.playNotification(NotificationMessage.NO_CANVAS);
            return;
        }
        if(activeCanvas.getSelectedArea() == null) {
            TransferableImage transferableImage = new TransferableImage(activeCanvas.getActiveLayer().getImage());

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(transferableImage, null);

            int index = activeCanvas.getActiveLayerIndex();
            Layer layer = activeCanvas.getActiveLayer();

            Controller.deleteLayerFromActiveCanvas(index);

            LayerDeletionAction thisAction = new LayerDeletionAction(layer, index);
            activeCanvas.undoManager.addEdit(thisAction);
        } else {
            BufferedImage i = activeCanvas.getActiveLayer().getImage();
            BufferedImage originalImage = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
            originalImage.getGraphics().drawImage(i, 0, 0, null);

            Rectangle selectedArea = activeCanvas.getSelectedArea();
            BufferedImage selectedPart = i.getSubimage(selectedArea.x, selectedArea.y, selectedArea.width, selectedArea.height);
            TransferableImage transferableImage = new TransferableImage(selectedPart);

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(transferableImage, null);

            activeCanvas.getActiveLayer().clearRect(selectedArea);
            ArrayList<PixelChange> pixelChanges = activeCanvas.getActiveLayer().getImageDifferences(originalImage);

            EditAction thisAction = new EditAction(activeCanvas.getActiveLayerIndex(), pixelChanges);
            activeCanvas.undoManager.addEdit(thisAction);
        }
    }

    public static void paste() {
        if(activeCanvas == null) {
            NotificationPanel.playNotification(NotificationMessage.NO_CANVAS);
            return;
        }
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if(transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                BufferedImage image = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
                if(image.getWidth() > activeCanvas.getWidth()) {
                    NotificationPanel.playNotification(NotificationMessage.CLIPBOARD_TOO_WIDE);
                    return;
                }
                if(image.getHeight() > activeCanvas.getHeight()) {
                    NotificationPanel.playNotification(NotificationMessage.CLIPBOARD_TOO_TALL);
                    return;
                }

                Layer pasteLayer = new Layer(activeCanvas);
                pasteLayer.getImage().getGraphics().drawImage(image, 0, 0, null);
                pasteLayer.setName("Layer " + activeCanvas.nextNameNumber());

                activeCanvas.addLayer(pasteLayer, activeCanvas.getActiveLayerIndex());
                Frame.layerPanel.addLayer(pasteLayer, activeCanvas.getActiveLayerIndex());

                PasteAction pasteAction = new PasteAction(pasteLayer, activeCanvas.layers.indexOf(pasteLayer));
                activeCanvas.undoManager.addEdit(pasteAction);
            } catch (UnsupportedFlavorException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            NotificationPanel.playNotification(NotificationMessage.CLIPBOARD_NOT_IMAGE);
        }
    }

    public static void delete() {
        if(activeCanvas == null) {
            NotificationPanel.playNotification(NotificationMessage.NO_CANVAS_DELETE);
            return;
        }

        if(activeCanvas.getSelectedArea() == null) {
            int index = activeCanvas.getActiveLayerIndex();
            Layer layer = activeCanvas.getActiveLayer();

            Controller.deleteLayerFromActiveCanvas(index);

            LayerDeletionAction thisAction = new LayerDeletionAction(layer, index);
            activeCanvas.undoManager.addEdit(thisAction);
        } else {
            BufferedImage i = activeCanvas.getActiveLayer().getImage();
            BufferedImage originalImage = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_INT_ARGB);
            originalImage.getGraphics().drawImage(i, 0, 0, null);

            Rectangle selectedArea = activeCanvas.getSelectedArea();
            activeCanvas.getActiveLayer().clearRect(selectedArea);

            ArrayList<PixelChange> pixelChanges = activeCanvas.getActiveLayer().getImageDifferences(originalImage);

            EditAction thisAction = new EditAction(activeCanvas.getActiveLayerIndex(), pixelChanges);
            activeCanvas.undoManager.addEdit(thisAction);
        }
    }

    public static boolean isActiveLayer(Layer layer) {
        return layer == activeCanvas.getActiveLayer();
    }

    private static BaseTool activeTool;

    public static void setActiveTool(BaseTool tool) {
        activeTool = tool;
        //todo change to get/set
        Frame.canvasPanel.activeTool = activeTool;
        ToolSettings.onNewTool(tool);
    }

    public static BaseTool getActiveTool() {
        return activeTool;
    }
}
