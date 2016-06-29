package pl.edu.mimuw.forum.ui.controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.edu.mimuw.forum.example.Dummy;
import pl.edu.mimuw.forum.exceptions.ApplicationException;
import pl.edu.mimuw.forum.ui.bindings.MainPaneBindings;
import pl.edu.mimuw.forum.ui.helpers.DialogHelper;
import pl.edu.mimuw.forum.ui.history.HistoryOperations;
import pl.edu.mimuw.forum.ui.models.NodeViewModel;
import pl.edu.mimuw.forum.ui.operation.Add;
import pl.edu.mimuw.forum.ui.operation.Remove;
import pl.edu.mimuw.forum.ui.tree.ForumTreeItem;
import pl.edu.mimuw.forum.ui.tree.TreeLabel;

/**
 * Kontroler glownego widoku reprezentujacego forum.
 * Widok sklada sie z drzewa zawierajacego wszystkie wezly forum oraz
 * panelu z polami do edycji wybranego wezla.
 * @author konraddurnoga
 */
public class MainPaneController implements Initializable {

	/**
	 * Korzen drzewa-modelu forum.
	 */
	private NodeViewModel document;

	/**
	 * Wiazania stosowane do komunikacji z {@link pl.edu.mimuw.forum.ui.controller.ApplicationController }.
	 */
	private MainPaneBindings bindings;

	public static HistoryOperations history;
	public static int redoFlag = 0;
	/**
	 * Widok drzewa forum (wyswietlany w lewym panelu).
	 */
	@FXML
	private TreeView<NodeViewModel> treePane;

	/**
	 * Kontroler panelu wyswietlajacego pola do edycji wybranego wezla w drzewie.
	 */
	@FXML
	private DetailsPaneController detailsController;
	

	public MainPaneController() {
		bindings = new MainPaneBindings();
		history = new HistoryOperations();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		BooleanBinding nodeSelectedBinding = Bindings.isNotNull(treePane.getSelectionModel().selectedItemProperty());
		bindings.nodeAdditionAvailableProperty().bind(nodeSelectedBinding);
		bindings.nodeRemovaleAvailableProperty()
				.bind(nodeSelectedBinding.and(
						Bindings.createBooleanBinding(() -> getCurrentTreeItem().orElse(null) != treePane.getRoot(),
								treePane.rootProperty(), nodeSelectedBinding)));
		
		bindings.hasChangesProperty().set(true);		// TODO Nalezy ustawic na true w przypadku, gdy w widoku sa zmiany do
														// zapisania i false wpp, w odpowiednim miejscu kontrolera (niekoniecznie tutaj)
														// Spowoduje to dodanie badz usuniecie znaku '*' z tytulu zakladki w ktorej
		//if(history.getUndo().isEmpty())bindings.undoAvailableProperty().set(false);												// otwarty jest plik - '*' oznacza niezapisane zmiany
		bindings.undoAvailableProperty().set(true);
		
		//if(history.getRedo().isEmpty())bindings.redoAvailableProperty().set(false);
		 bindings.redoAvailableProperty().set(true);		// Podobnie z undo i redo
	}

	public MainPaneBindings getPaneBindings() {
		return bindings;
	}

	/**
	 * Otwiera plik z zapisem forum i tworzy reprezentacje graficzna wezlow forum.
	 * @param file
	 * @return
	 * @throws ApplicationException
	 */
	public Node open(File file) throws ApplicationException {
		if (file != null) {
			XStream xstream = new XStream(new DomDriver("Unicode"));
	        ObjectInputStream in = null;
	        try {
	            FileInputStream fistream = new FileInputStream(file);
	            Reader rd = new BufferedReader(new InputStreamReader(fistream, "UTF-8"));
	            in = xstream.createObjectInputStream(rd);
	            xstream.addImplicitCollection(pl.edu.mimuw.forum.data.Node.class, "children");
	            document = ((pl.edu.mimuw.forum.data.Node) in.readObject()).getModel();
	        }
	        catch (UnsupportedEncodingException e) {
	        	throw new ApplicationException(e);
	        }catch (FileNotFoundException e) {
	        	throw new ApplicationException(e);
	        }catch (IOException e) {
	        	throw new ApplicationException(e);
	        }catch (ClassNotFoundException e) {
	        	throw new ApplicationException(e);
	        }
	        finally {
	        	try {
	        		if (in != null) in.close();
	        	} catch (IOException e) {
	        		throw new ApplicationException(e);
	        	}
	        }
		} else {
			document = new NodeViewModel("Welcome to a new forum", "Admin");
		}
		
		/** Dzieki temu kontroler aplikacji bedzie mogl wyswietlic nazwe pliku jako tytul zakladki.
		 * Obsluga znajduje sie w {@link pl.edu.mimuw.forum.ui.controller.ApplicationController#createTab }
		 */
		getPaneBindings().fileProperty().set(file);

		return openInView(document);
	}

	/**
	 * Zapisuje aktualny stan forum do pliku.
	 * @throws ApplicationException
	 */
	public void save() throws ApplicationException {
		//TODO Tutaj umiescic wywolanie obslugi zapisu drzewa z widoku do pliku
		/**
		 * Obiekt pliku do ktorego mamy zapisac drzewo znajduje sie w getPaneBindings().fileProperty().get()
		 */
		if (document != null) {
			System.out.println("On save " + document.toNode());	//Tak tworzymy drzewo do zapisu z modelu aplikacji
		}
		XStream xstream = new XStream(new DomDriver("Unicode"));
        ObjectOutputStream out = null;
        try {
            PrintWriter pwriter = new PrintWriter(getPaneBindings().fileProperty().get());
            out = xstream.createObjectOutputStream(pwriter, "Forum");
            xstream.addImplicitCollection(pl.edu.mimuw.forum.data.Node.class, "children");
            out.writeObject(document.toNode());
        }
        catch(IOException e){
        	throw new ApplicationException(e);
        }
        finally{
        	try{
        		out.close();
        	}
        	catch(IOException e){
        		throw new ApplicationException(e);
        	}
        }
	}
	
	/**
	 * Cofa ostatnio wykonana operacje na forum.
	 * @throws ApplicationException
	 */
	public void undo() throws ApplicationException {
		redoFlag = 1;
		System.out.println("On undo");	//TODO Tutaj umiescic obsluge undo
		history.undo();
		bindings.redoAvailableProperty().set(true);
		redoFlag = 0;
	}

	/**
	 * Ponawia ostatnia cofnieta operacje na forum.
	 * @throws ApplicationException
	 */
	public void redo() throws ApplicationException {
		redoFlag = 1;
		System.out.println("On redo");	//TODO Tutaj umiescic obsluge redo
		history.redo();
		redoFlag = 0;
	}

	/**
	 * Podaje nowy wezel jako ostatnie dziecko aktualnie wybranego wezla.
	 * @param node
	 * @throws ApplicationException
	 */
	public void addNode(NodeViewModel node) throws ApplicationException {
		getCurrentNode().ifPresent(currentlySelected -> {
			currentlySelected.getChildren().add(node);		// Zmieniamy jedynie model, widok (TreeView) jest aktualizowany z poziomu
															// funkcji nasluchujacej na zmiany w modelu (zob. metode createViewNode ponizej)
		});
	}

	/**
	 * Usuwa aktualnie wybrany wezel.
	 */
	public void deleteNode() {
		getCurrentTreeItem().ifPresent(currentlySelectedItem -> {
			TreeItem<NodeViewModel> parent = currentlySelectedItem.getParent();

			NodeViewModel parentModel;
			NodeViewModel currentModel = currentlySelectedItem.getValue();
			if (parent == null) {
				return; // Blokujemy usuniecie korzenia - TreeView bez korzenia jest niewygodne w obsludze
			} else {
				parentModel = parent.getValue();
				parentModel.getChildren().remove(currentModel); // Zmieniamy jedynie model, widok (TreeView) jest aktualizowany z poziomu
																// funkcji nasluchujacej na zmiany w modelu (zob. metode createViewNode ponizej)
			}

		});
	}

	private Node openInView(NodeViewModel document) throws ApplicationException {
		Node view = loadFXML();

		treePane.setCellFactory(tv -> {
			try {
				//Do reprezentacji graficznej wezla uzywamy niestandardowej klasy wyswietlajacej 2 etykiety
				return new TreeLabel();
			} catch (ApplicationException e) {
				DialogHelper.ShowError("Error creating a tree cell.", e);
				return null;
			}
		});

		ForumTreeItem root = createViewNode(document);
		root.addEventHandler(TreeItem.<NodeViewModel> childrenModificationEvent(), event -> {
			if (event.wasAdded()) {
				System.out.println("Adding to " + event.getSource());
			}
			
			if (event.wasRemoved()) {
				System.out.println("Removing from " + event.getSource());
			}
		});

		treePane.setRoot(root);

		for (NodeViewModel w : document.getChildren()) {
			addToTree(w, root);
		}

		expandAll(root);

		treePane.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> onItemSelected(oldValue, newValue));

		return view;
	}
	
	private Node loadFXML() throws ApplicationException {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setLocation(getClass().getResource("/fxml/main_pane.fxml"));

		try {
			return loader.load();
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
	}
	
	private Optional<? extends NodeViewModel> getCurrentNode() {
		return getCurrentTreeItem().<NodeViewModel> map(TreeItem::getValue);
	}

	private Optional<TreeItem<NodeViewModel>> getCurrentTreeItem() {
		return Optional.ofNullable(treePane.getSelectionModel().getSelectedItem());
	}

	private void addToTree(NodeViewModel node, ForumTreeItem parentViewNode, int position) {
		ForumTreeItem viewNode = createViewNode(node);

		List<TreeItem<NodeViewModel>> siblings = parentViewNode.getChildren();
		siblings.add(position == -1 ? siblings.size() : position, viewNode);

		node.getChildren().forEach(child -> addToTree(child, viewNode));
	}

	private void addToTree(NodeViewModel node, ForumTreeItem parentViewNode) {
		addToTree(node, parentViewNode, -1);
	}

	private void removeFromTree(ForumTreeItem viewNode) {
		viewNode.removeChildListener();
		TreeItem<NodeViewModel> parent = viewNode.getParent();
		if (parent != null) {
			viewNode.getParent().getChildren().remove(viewNode);
		} else {
			treePane.setRoot(null);
		}
	}

	private ForumTreeItem createViewNode(NodeViewModel node) {
		ForumTreeItem viewNode = new ForumTreeItem(node);
		viewNode.setChildListener(change -> {
			// wywolywanem, gdy w modelu dla tego wezla zmieni sie zawartosc kolekcji dzieci
			while (change.next()) {
				if (change.wasAdded()) {
					int i = change.getFrom();
					for (NodeViewModel child : change.getAddedSubList()) {
						if(redoFlag == 0)history.add(new Add(child, node, i));
						addToTree(child, viewNode, i);	// uwzgledniamy nowy wezel modelu w widoku
						i++;
					}
				}

				if (change.wasRemoved()) {
					for (int i = change.getFrom(); i <= change.getTo(); ++i) {
						if(redoFlag == 0)history.add(new Remove(viewNode.getChildren().get(i).getValue(), node));
						removeFromTree((ForumTreeItem) viewNode.getChildren().get(i)); // usuwamy wezel modelu z widoku
					}
				}
			}
		});

		return viewNode;
	}

	private void expandAll(TreeItem<NodeViewModel> item) {
		item.setExpanded(true);
		item.getChildren().forEach(this::expandAll);
	}

	private void onItemSelected(TreeItem<NodeViewModel> oldItem, TreeItem<NodeViewModel> newItem) {
		detailsController.setModel(newItem != null ? newItem.getValue() : null);
	}

}
