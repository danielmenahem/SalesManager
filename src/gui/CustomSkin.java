package gui;

import com.sun.javafx.scene.control.skin.ListViewSkin;

import javafx.scene.control.ListView;

public class CustomSkin<T> extends ListViewSkin<T>{

	public CustomSkin(ListView<T> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public void refresh() {
		super.flow.rebuildCells();
	}
	
	
	@SuppressWarnings("unchecked")
    static <T> CustomSkin<T> cast(Object obj) {
        return (CustomSkin<T>)obj;
    }
	
}
