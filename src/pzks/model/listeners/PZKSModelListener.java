package pzks.model.listeners;

import java.util.EventListener;
import javax.swing.event.ChangeEvent;

public interface PZKSModelListener extends EventListener 
{
    void changeState(ChangeEvent event);
}