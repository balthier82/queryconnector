/*
	QueryConnector - Attach a query to a Calc document
    Copyright (C) 2013 Enrico Giuseppe Messina

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

import com.sun.star.script.provider.XScriptContext;

import com.sun.star.uno.UnoRuntime;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XText;
import com.sun.star.beans.PropertyValue;
import com.sun.star.awt.ActionEvent;
import com.meserico.queryconnector.DBConnectorWizard;
import com.meserico.queryconnector.InfoDialog;
import com.meserico.queryconnector.WizardListener;
import com.meserico.queryconnector.ExceptionDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XController;
import com.sun.star.sheet.XSpreadsheetView;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.view.XSelectionSupplier;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.sheet.XCellAddressable;
import com.sun.star.table.CellAddress;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import com.sun.star.document.XDocumentPropertiesSupplier;
import com.sun.star.beans.XPropertyContainer;
import com.sun.star.beans.XPropertySet;
import com.sun.star.util.XModifiable;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.XComponentContext;
import com.sun.star.container.XNameContainer;
import com.sun.star.container.XNameAccess;
import com.sun.star.xml.AttributeData;
import com.sun.star.frame.XStorable;
import java.util.UUID;
import com.sun.star.util.XNumberFormatsSupplier;
import com.sun.star.util.XNumberFormats;
import com.sun.star.util.XModifiable;
import com.sun.star.sheet.XFunctionAccess;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import com.sun.star.rdf.XDocumentMetadataAccess;
import com.sun.star.rdf.XRepositorySupplier;
import com.sun.star.rdf.XRepository;
import com.sun.star.rdf.XURI;
import com.sun.star.rdf.XNamedGraph;
import com.sun.star.rdf.URI;
import com.sun.star.rdf.Literal;
import com.sun.star.rdf.XLiteral;
import com.sun.star.rdf.XResource;
import org.json.JSONObject;
import com.sun.star.embed.XStorage;
import com.sun.star.embed.ElementModes;
import com.sun.star.document.XStorageBasedDocument;
import com.sun.star.io.XStream;
import com.sun.star.io.XInputStream;
import com.sun.star.xml.dom.XDocumentBuilder;
import com.sun.star.xml.dom.XDocument;
import com.sun.star.xml.dom.XElement;
import com.sun.star.xml.xpath.XXPathAPI;
import com.sun.star.xml.xpath.XXPathObject;
import com.sun.star.xml.xpath.XPathObjectType;
import com.sun.star.container.XEnumeration;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.container.XIndexAccess;
import com.sun.star.drawing.XDrawPageSupplier;
import com.sun.star.drawing.XDrawPage;
import com.sun.star.comp.helper.Bootstrap;

public class QueryConnector implements WizardListener
{
	private static final String QUERY_PROPERTY = "com.meserico.libreoffice.Query";
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/YYYY HH.mm.ss");
	private static Map<String, Map<String, String>> languages;
	private static final String SHEET_ID = "QueryConnectorSheetID";
	
	//########################################################################################
	
	/** GESTIONE TRADUZIONI **/
	
	static {
		languages = new HashMap<String, Map<String, String>>();
		//lingua italiana
		HashMap<String, String> italy = new HashMap<String, String>();
		italy.put("ERROR_DIALOG_TITLE", "Errore...");
		italy.put("CONFIRM_DIALOG_TITLE", "Conferma...");
		italy.put("INFO_DIALOG_TITLE", "Informazioni...");
		italy.put("ATTACH_ON_EXISTENT_QUERY_CONFIRM", "La cella selezionata appartiene ad una area contenente una query agganciata.\nProcedendo "+
					"con l'aggancio di una nuova query i vecchi dati andranno definitivamente perduti.\n" +
					"Vuoi continuare?");
		italy.put("QUERY_NOY_PRESENT_ERROR", "La query associata alla cella non e' presente nel documento Calc.\n"+
						  "Impossibile eseguire l'aggiornamento");
		italy.put("CELL_DOESNT_HAVE_QUERY_ERROR", "La cella selezionata non ha query agganciate.");
		italy.put("SELECT_SINGLE_CELL_ERROR", "Selezionare una singola cella.");
		italy.put("DOCUMENT_DOESNT_HAVE_QUERY_ERROR", "Il documento non ha query agganciate.");
		italy.put("ALL_QUERIES_UPDATED", "Tutte le query sono state aggiornate.");
		italy.put("SPREADSHEET_NOT_EXISTS", "Il foglio a cui è associato la query non esiste.");
		languages.put(Locale.ITALY.getLanguage(), italy);
		//lingua inglese
		HashMap<String, String> english = new HashMap<String, String>();
		english.put("ERROR_DIALOG_TITLE", "Error...");
		english.put("CONFIRM_DIALOG_TITLE", "Confirm...");
		english.put("INFO_DIALOG_TITLE", "Information...");
		english.put("ATTACH_ON_EXISTENT_QUERY_CONFIRM", "The selected cell belongs to an area with an attached query.\n"+
				"If you proceed with a new attachment, the old data will be lost.\n"+
				"Do you want to continue?");
		english.put("QUERY_NOY_PRESENT_ERROR", "The query associated with the selected cell is not present in this document.\n"+
					"Can not proceed with the data upgrade.");
		english.put("CELL_DOESNT_HAVE_QUERY_ERROR", "The selected cell doesn't have an associated query.");
		english.put("SELECT_SINGLE_CELL_ERROR", "Please, select a single cell.");
		english.put("DOCUMENT_DOESNT_HAVE_QUERY_ERROR", "The document not has attached queries.");
		english.put("ALL_QUERIES_UPDATED", "All queries updated.");
		english.put("SPREADSHEET_NOT_EXISTS", "The query Spreadsheet is not found.");
		languages.put(Locale.ENGLISH.getLanguage(), english);
		languages.put(Locale.UK.getLanguage(), english);
		languages.put(Locale.US.getLanguage(), english);
	}
	
	private static String tr(String trName){
		Locale locale = Locale.getDefault();
		Map<String, String> selectedLanguage = languages.get(locale.getLanguage());
		String value = null;
		if(selectedLanguage == null)
			value = languages.get(Locale.ENGLISH.getLanguage()).get(trName);
		else value = selectedLanguage.get(trName);
		if(value == null)
			value = "{MISSING TRANSLATION FOR '"+trName+"' KEY}";
		return value;
	}
	
	//########################################################################################
	
	/** Gestione degli script **/

    public static void attach(XScriptContext ctxt, ActionEvent e)
        throws Exception
    {
        QueryConnector.attach(ctxt);
    }

    public static void attach(XScriptContext ctxt) throws Exception {
		QueryConnector connector = null;
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			connector = new QueryConnector(ctxt);
			connector.attach();
		}catch(Exception ex){
			if(connector != null)
				connector.enableEdit();
			ExceptionDialog.show(null, ex);
		}
    }
	
	public static void update(XScriptContext ctxt, ActionEvent e)
        throws Exception
    {
        QueryConnector.update(ctxt);
    }

    public static void update(XScriptContext ctxt) throws Exception {
		QueryConnector connector = null;
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			connector = new QueryConnector(ctxt);
			connector.update();
		}catch(Exception ex){
			if(connector != null)
				connector.enableEdit();
			ExceptionDialog.show(null, ex);
		}
    }
	
	public static void info(XScriptContext ctxt, ActionEvent e)
        throws Exception
    {
        QueryConnector.update(ctxt);
    }

    public static void info(XScriptContext ctxt) throws Exception {
		QueryConnector connector = null;
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			connector = new QueryConnector(ctxt);
			connector.info();
		}catch(Exception ex){
			if(connector != null)
				connector.enableEdit();
			ExceptionDialog.show(null, ex);
		}
    }
	
	public static void updateAll(XScriptContext ctxt, ActionEvent e)
        throws Exception
    {
        QueryConnector.updateAll(ctxt);
    }

    public static void updateAll(XScriptContext ctxt) throws Exception {
		QueryConnector connector = null; 
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			connector = new QueryConnector(ctxt);
			connector.updateAll();
		}catch(Exception ex){
			if(connector != null)
				connector.enableEdit();
			ExceptionDialog.show(null, ex);
		}
    }
	
	public static void updateAllThroughModel(XComponentContext componentContext, XModel model) {
		QueryConnector connector = null; 
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			connector = new QueryConnector(model, componentContext);
			connector.updateAll();
		}catch(Exception ex){
			if(connector != null)
				connector.enableEdit();
			ExceptionDialog.show(null, ex);
		}
	}
	
	public static void silentUpdateAllThroughModel(XComponentContext componentContext, XModel model) {
		QueryConnector connector = null; 
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			connector = new QueryConnector(model, componentContext);
			connector.setSilent(true);
			connector.updateAll();
		}catch(Exception ex){
			if(connector != null)
				connector.enableEdit();
			ExceptionDialog.show(null, ex);
		}
	}
	
	//########################################################################################
	
	/** Gestione finestre di dialog **/
	
	private static void error(String msg){
		JOptionPane.showMessageDialog(null, msg, tr("ERROR_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);
	}
	
	private static void info(String msg){
		JOptionPane.showMessageDialog(null, msg, tr("INFO_DIALOG_TITLE"), JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static boolean confirm(String msg){
		return JOptionPane.showConfirmDialog(null, msg, tr("CONFIRM_DIALOG_TITLE"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}
	
	//########################################################################################
	
	/** QueryConnector class **/
	
	private XModel model;
	private XController controller;
	private XSpreadsheetView view;
	private XSelectionSupplier selectionSupplier;
	private XCell selectedCell;
	private CellAddress selectedCellAddress;
	private XSpreadsheet activeSheet;
	private XCellRange activeSheetCellRange;
	private XPropertyContainer userDefinedPropertiesContainer;
	private XPropertySet userDefinedPropertiesSet;
	private XMultiComponentFactory serviceManager;
	private XComponentContext componentContext;
	private XStorable storable;
	private XNumberFormats numberFormats;
	private XFunctionAccess functionAccess;
	private XStorage documentStorage;
	private Settings settings;
	private XModifiable modifiable;
	private XSpreadsheetDocument document;
	private XSpreadsheets sheetCollection;
	private boolean silent;
	
	private QueryConnector(XScriptContext context) throws Exception{
		this(context.getDocument(), context.getComponentContext());
	}
	
	private QueryConnector(XModel model, XComponentContext componentContext) throws Exception{
		this.silent = false;
		this.model = model;
		this.componentContext = componentContext;
		this.serviceManager = this.componentContext.getServiceManager();
		this.document = UnoRuntime.queryInterface(XSpreadsheetDocument.class, this.model);
		this.sheetCollection = this.document.getSheets();
		this.numberFormats = UnoRuntime.queryInterface(XNumberFormatsSupplier.class, this.model).getNumberFormats();
		this.storable = UnoRuntime.queryInterface(XStorable.class, this.model);
		this.modifiable = UnoRuntime.queryInterface(XModifiable.class, this.model);
		XDocumentPropertiesSupplier documentProperties = UnoRuntime.queryInterface(XDocumentPropertiesSupplier.class, this.model);
		documentStorage = UnoRuntime.queryInterface(XStorageBasedDocument.class, model).getDocumentStorage();
		this.settings = new Settings(model);
		this.userDefinedPropertiesContainer = documentProperties.getDocumentProperties().getUserDefinedProperties();
		this.userDefinedPropertiesSet = UnoRuntime.queryInterface(XPropertySet.class, this.userDefinedPropertiesContainer);
		this.controller = model.getCurrentController();
		this.view = UnoRuntime.queryInterface(com.sun.star.sheet.XSpreadsheetView.class, controller);
		this.activeSheet = view.getActiveSheet();
		this.activeSheetCellRange = UnoRuntime.queryInterface(XCellRange.class, activeSheet);
		this.selectionSupplier = UnoRuntime.queryInterface(com.sun.star.view.XSelectionSupplier.class, view);
		Object selection = selectionSupplier.getSelection();
		this.selectedCell = UnoRuntime.queryInterface(com.sun.star.table.XCell.class, selection);
		this.functionAccess = UnoRuntime.queryInterface(XFunctionAccess.class, 
			this.serviceManager.createInstanceWithContext("com.sun.star.sheet.FunctionAccess", this.componentContext));
	}
	
	public void setSilent(boolean silent){
		this.silent = silent;
	}
	
	private double dateValue(String date) throws Exception {
		return Double.parseDouble(this.functionAccess.callFunction("DateValue", new String[]{date}).toString());
	}
	
	private void disableEdit() {
		this.controller.getFrame().getContainerWindow().setEnable(false);
	}
	
	protected void enableEdit() {
		this.controller.getFrame().getContainerWindow().setEnable(true);
	}
	
	private void attach() throws Exception {
		if(this.selectedCell != null){
			disableEdit();
			String queryName = getCellUserProperty(this.selectedCell, QUERY_PROPERTY);
			boolean cont = true;
			if(queryName != null && this.settings.hasQuery(queryName)){
				cont = confirm(tr("ATTACH_ON_EXISTENT_QUERY_CONFIRM"));
				if(cont) clearQuery(queryName);
			}
			if(cont){
				this.selectedCellAddress = UnoRuntime.<XCellAddressable>queryInterface(XCellAddressable.class, this.selectedCell).getCellAddress();	
				DBConnectorWizard wizard = new DBConnectorWizard(this);
				wizard.setVisible(true);
			}else enableEdit();
		}else
			error(tr("SELECT_SINGLE_CELL_ERROR"));
	}
	
	private void update() throws Exception {
		if(this.selectedCell != null){
			this.selectedCellAddress = UnoRuntime.<XCellAddressable>queryInterface(XCellAddressable.class, this.selectedCell).getCellAddress();	
			String queryName = getCellUserProperty(this.selectedCell, QUERY_PROPERTY);
			if(queryName != null){
				if(!this.settings.hasQuery(queryName))
					error(tr("QUERY_NOY_PRESENT_ERROR"));
				else{
					Query query = settings.getQuery(queryName);
					XSpreadsheet sheet = getSpreadsheetById(query.getSheetID());
					if(sheet == null)
						error(tr("SPREADSHEET_NOT_EXISTS"));
					else{
						disableEdit();
						clearArea(queryName, false);
						CellAddress address = new CellAddress();
						address.Column = query.getStartColumn();
						address.Row = query.getStartRow();
						this.loadData(sheet, address, query, true);
						enableEdit();
					}
				}
			}else
				error(tr("CELL_DOESNT_HAVE_QUERY_ERROR"));
		}else
			error(tr("SELECT_SINGLE_CELL_ERROR"));
	}
	
	private void updateAll() throws Exception {
		List<String> queries = settings.getQueryNames();
		if(queries.size() == 0){
			error(tr("DOCUMENT_DOESNT_HAVE_QUERY_ERROR"));
		}else{
			boolean foundAtLeastOne = false;
			for(String queryName : queries){
				disableEdit();
				Query query = settings.getQuery(queryName);
				XSpreadsheet sheet = getSpreadsheetById(query.getSheetID());
				if(sheet != null) {
					foundAtLeastOne = true;
					clearArea(queryName, false);
					CellAddress address = new CellAddress();
					address.Column = query.getStartColumn();
					address.Row = query.getStartRow();
					this.loadData(sheet, address, query, true);
				}
				enableEdit();
			}
			if(!silent){
				if(foundAtLeastOne)
					info(tr("ALL_QUERIES_UPDATED"));
				else error(tr("DOCUMENT_DOESNT_HAVE_QUERY_ERROR"));
			}
		}
	}
	
	private void info() throws Exception {
		if(this.selectedCell != null){
			this.selectedCellAddress = UnoRuntime.<XCellAddressable>queryInterface(XCellAddressable.class, this.selectedCell).getCellAddress();	
			String queryName = getCellUserProperty(this.selectedCell, QUERY_PROPERTY);
			if(queryName != null){
				if(!this.settings.hasQuery(queryName))
					error(tr("QUERY_NOY_PRESENT_ERROR"));
				else{
					Query query = this.settings.getQuery(queryName);
					XCell cell = this.activeSheetCellRange.getCellByPosition(query.getStartColumn(), query.getStartRow());
					XPropertySet cellProps = UnoRuntime.queryInterface(XPropertySet.class, cell);
					String absoluteName = cellProps.getPropertyValue("AbsoluteName").toString();
					int dotIdx = absoluteName.lastIndexOf(".");
					String cellName = absoluteName.substring(dotIdx+1, absoluteName.length());
					cellName = cellName.replaceAll("\\$", "");
					new InfoDialog(query.getURL(), query.getUsername(), cellName, ""+query.getRowCount(), 
						""+query.getColumnCount(), query.getDriverClass()).setVisible(true);
				}
			}else
				error(tr("CELL_DOESNT_HAVE_QUERY_ERROR"));
		}else
			error(tr("SELECT_SINGLE_CELL_ERROR"));
	}
	
	private void clearArea(String queryName, boolean removeUDP) throws Exception {
		Query query = settings.getQuery(queryName);
		XSpreadsheet sheet = this.getSpreadsheetById(query.getSheetID());
		XCellRange sheetRange = UnoRuntime.queryInterface(XCellRange.class, sheet);
		for(int i=query.getStartRow(); i<query.getRowCount(); i++)
			for(int j=query.getStartColumn(); j<query.getColumnCount(); j++){
				XCell cell = sheetRange.getCellByPosition(j, i);
				XTextRange cellText = UnoRuntime.queryInterface(XTextRange.class, cell);
				cellText.setString("");
				if(removeUDP)
					removeCellUserProperty(cell, QUERY_PROPERTY);
			}
	}
	
	private void clearQuery(String queryName) throws Exception {
		clearArea(queryName, true);
		settings.removeQuery(queryName);
	}
	
	private void loadData(XSpreadsheet sheet, CellAddress startCell, Query query, boolean saveConnectionInfo) throws Exception{
		XCellRange cellRange = UnoRuntime.queryInterface(XCellRange.class, sheet);
		com.sun.star.lang.Locale locale = new com.sun.star.lang.Locale();
		//effettuo la query sul db
		Class.forName(query.getDriverClass());
		Connection con = DriverManager.getConnection(query.getURL(), query.getUsername(), query.getPassword());
		Statement stmt = con.createStatement();
		ResultSet result = stmt.executeQuery(query.getQuery());
		ResultSetMetaData metaData = result.getMetaData();
		int columnCount = metaData.getColumnCount();
		//intestazione del risultato (nomi delle colonne)
		for(int i=1, j=startCell.Column; i<=columnCount; i++, j++){
			XCell curCellHeader = cellRange.getCellByPosition(j, startCell.Row);
			XTextRange currentCellHeaderText = UnoRuntime.queryInterface(XTextRange.class, curCellHeader);
			currentCellHeaderText.setString(metaData.getColumnLabel(i));
			if(saveConnectionInfo)
				setCellUserProperty(curCellHeader, QUERY_PROPERTY, query.getName());
		}
		//contenuto della query
		int rowCount = 1;
		int rowIdx = startCell.Row + 1;
		while(result.next()){
			for(int i=1, j=startCell.Column; i<=columnCount; i++, j++){
				XCell curCellData = cellRange.getCellByPosition(j, rowIdx);
				XPropertySet cellDataProps = UnoRuntime.queryInterface(XPropertySet.class, curCellData);
				XTextRange currentCellDataText = UnoRuntime.queryInterface(XTextRange.class, curCellData);
				Object cellValue = result.getObject(i);
				if(cellValue instanceof java.sql.Date || cellValue instanceof java.sql.Time || cellValue instanceof java.util.Date){
					cellDataProps.setPropertyValue("NumberFormat", this.numberFormats.queryKey("GG/MM/AAAA HH.MM.SS", locale, true));
					curCellData.setValue(dateValue(DATE_FORMATTER.format((java.util.Date) cellValue)));
				}else if(cellValue instanceof Number){
					cellDataProps.setPropertyValue("NumberFormat", this.numberFormats.queryKey("#", locale, true));
					Number number = (Number) cellValue;
					curCellData.setValue(number.doubleValue());
				}else if(cellValue == null){
					currentCellDataText.setString("");
				}else
					currentCellDataText.setString(cellValue.toString());
				if(saveConnectionInfo)
					setCellUserProperty(curCellData, QUERY_PROPERTY, query.getName());
			}
			rowCount++;
			rowIdx++;
		}
		result.close();
		stmt.close();
		con.close();
		//aggiono le informazioni sul range dei dati selezionato nella query
		query.setPositionAndArea(startCell.Column, startCell.Row, columnCount, rowCount);
		//salvo le informazioni sul documento
		if(saveConnectionInfo)
			settings.saveQuery(query);
	}
	
	//ritorna gli User Defined Attributes di un oggetto XCell
	private XNameContainer getCellUDA(XCell cell) throws Exception{
		XPropertySet property = UnoRuntime.queryInterface(XPropertySet.class, cell);
		Object obj = property.getPropertyValue("UserDefinedAttributes");
		return UnoRuntime.queryInterface(XNameContainer.class, obj);
	}
	
	//setta gli User Defined Attributes di un oggetto XCell
	private void setCellUDA(XCell cell, XNameContainer container) throws Exception{
		XPropertySet property = UnoRuntime.queryInterface(XPropertySet.class, cell);
		property.setPropertyValue("UserDefinedAttributes", container);
	}
	
	//rimuove una proprietà User Defined di una cella
	private void removeCellUserProperty(XCell cell, String name) throws Exception{
		XNameContainer uda = getCellUDA(cell);
		XNameAccess udac = UnoRuntime.queryInterface(XNameAccess.class, uda);
		if(udac.hasByName(name))
			uda.removeByName(name);
		setCellUDA(cell, uda);
	}
	
	//setta una proprietà User Defined di una cella
	private void setCellUserProperty(XCell cell, String name, String value) throws Exception{
		XNameContainer uda = getCellUDA(cell);
		XNameAccess udac = UnoRuntime.queryInterface(XNameAccess.class, uda);
		if(udac.hasByName(name))
			uda.removeByName(name);
		AttributeData data = new AttributeData();
		data.Type = "CDATA";
		data.Value = value;
		uda.insertByName(name, data);
		setCellUDA(cell, uda);
	}
	
	//ritorna una proprietà User Defined di una cella
	private String getCellUserProperty(XCell cell, String name) throws Exception{
		XNameContainer uda = getCellUDA(cell);
		XNameAccess udac = UnoRuntime.queryInterface(XNameAccess.class, uda);
		if(!udac.hasByName(name))
			return null;
		AttributeData data = (AttributeData) udac.getByName(QUERY_PROPERTY);
		return data.Value;
	}
	
	//ritorna true se la proprietà User Defined  name di una cella esiste
	private boolean hasCellUserProperty(XCell cell, String name) throws Exception{
		XNameContainer uda = getCellUDA(cell);
		XNameAccess udac = UnoRuntime.queryInterface(XNameAccess.class, uda);
		return udac.hasByName(name);
	}
	
	private String getProperty(String name) throws Exception {
		return this.userDefinedPropertiesSet.getPropertyValue(name).toString();
	}
	
	private boolean hasProperty(String name) throws Exception{
		return this.userDefinedPropertiesSet.getPropertySetInfo().hasPropertyByName(name);
	}
	
	private void setProperty(String name, String value) throws Exception {
		if(!this.userDefinedPropertiesSet.getPropertySetInfo().hasPropertyByName(name))
			this.userDefinedPropertiesContainer.addProperty(name, (short) 256, (String) "");
		this.userDefinedPropertiesSet.setPropertyValue(name, (value == null)?"":value);
	}
	
	private void removeProperty(String name) throws Exception {
		if(!this.userDefinedPropertiesSet.getPropertySetInfo().hasPropertyByName(name))
			this.userDefinedPropertiesContainer.removeProperty(name);
	}
	
	private String getActiveSheetID() throws Exception {
		return this.getSheetID(this.activeSheet);
	}
	
	private String getSheetID(XSpreadsheet spreadsheet) throws Exception {
		XPropertySet sheetProperties = UnoRuntime.queryInterface(XPropertySet.class, spreadsheet);
		Object sheetUdaObj = sheetProperties.getPropertyValue("UserDefinedAttributes");
		XNameContainer sheetUda = UnoRuntime.queryInterface(XNameContainer.class, sheetUdaObj);
		XNameAccess sheetUdac = UnoRuntime.queryInterface(XNameAccess.class, sheetUda);
		XCellRange sheetRanges = UnoRuntime.queryInterface(XCellRange.class, spreadsheet);
		XCell firstCell = sheetRanges.getCellByPosition(0,0);
		XPropertySet firstCellProperties = UnoRuntime.queryInterface(XPropertySet.class, firstCell);
		Object firstCellUdaObj = firstCellProperties.getPropertyValue("UserDefinedAttributes");
		XNameContainer firstCellUda = UnoRuntime.queryInterface(XNameContainer.class, firstCellUdaObj);
		XNameAccess firstCellUdac = UnoRuntime.queryInterface(XNameAccess.class, firstCellUdaObj);
		AttributeData returnedData = null;
		if(!firstCellUdac.hasByName(SHEET_ID)){
			AttributeData data = new AttributeData();
			data.Type = "CDATA";
			data.Value = UUID.randomUUID().toString();
			sheetUda.insertByName(SHEET_ID, data);
			sheetProperties.setPropertyValue("UserDefinedAttributes", sheetUdaObj);
			returnedData = (AttributeData) sheetUda.getByName(SHEET_ID);
		}else returnedData = (AttributeData) firstCellUda.getByName(SHEET_ID);
		return returnedData.Value;
	}
	
	private XSpreadsheet getSpreadsheetById(String sheetId) throws Exception {
		XIndexAccess array = UnoRuntime.queryInterface(XIndexAccess.class, sheetCollection);
		for(int i=0; i<array.getCount(); i++){
			Object objSheet = array.getByIndex(i);
			XSpreadsheet currentSheet = UnoRuntime.queryInterface(XSpreadsheet.class, objSheet);
			String currentId = getSheetID(currentSheet);
			if(currentId != null && currentId.equals(sheetId))
				return currentSheet;
		}
		return null;
	}
	
	public void wizardCompleted(String queryStr, String driverClass, String url, String username, String password, boolean saveConnectionInfo){
		try{
			//preparo la query
			Query query = new Query(queryStr, driverClass, url, username, password);
			//carico i dati
			this.loadData(this.activeSheet, this.selectedCellAddress, query, saveConnectionInfo);
			//riattivo il controller
			enableEdit();
		}catch(Exception ex){
			ExceptionDialog.show(ex);
		}
	}
	
	public void wizardAborted(){
		enableEdit();
	}
	
	private class Settings {
	
		protected static final String NAMESPACE = "http://com.meserico.libreoffice/queryconnector";
		protected static final String QUERY_CONNECTOR_RDF = "queryconnector.rdf";
	
		private XNamedGraph settings;
		private XDocumentMetadataAccess metadataAccess;
		private XRepository rdfRepository;
		private XURI namespaceURI;
		private XResource resource;
	
		protected Settings(XModel model) throws Exception{
			settings = null;
			this.resource = UnoRuntime.queryInterface(XResource.class, model);
			this.namespaceURI = URI.create(componentContext, NAMESPACE);
			this.metadataAccess = UnoRuntime.queryInterface(XDocumentMetadataAccess.class, model);
			this.rdfRepository = UnoRuntime.queryInterface(XRepositorySupplier.class, model).getRDFRepository();
			XURI[] graphs = rdfRepository.getGraphNames();
			for(int i=0; i<graphs.length; i++)
				if(graphs[i].getLocalName().equals(QUERY_CONNECTOR_RDF))
					settings = rdfRepository.getGraph(graphs[i]);
			if(settings == null){
				XURI settingsURI = metadataAccess.addMetadataFile(QUERY_CONNECTOR_RDF, new XURI[]{namespaceURI});
				settings = rdfRepository.getGraph(settingsURI);
			}
		}
		
		protected List<String> getQueryNames() throws Exception {
			XEnumeration result = settings.getStatements(resource, null, null);
			List<String> queryNames = new ArrayList<String>();
			if(result.hasMoreElements()){
				com.sun.star.rdf.Statement stmt = (com.sun.star.rdf.Statement) result.nextElement();
				XURI uri = UnoRuntime.queryInterface(XURI.class, stmt.Predicate);
				if(uri != null && uri.getLocalName().toLowerCase().startsWith("query-"))
					queryNames.add(uri.getLocalName());
			}
			return queryNames;
		}
		
		protected String getQueryProperties(String queryName)  throws Exception {
			XURI queryURI = URI.create(componentContext, NAMESPACE + "/" + queryName);
			XEnumeration result = settings.getStatements(resource, queryURI, null);
			String content = null;
			if(result.hasMoreElements()){
				com.sun.star.rdf.Statement stmt = (com.sun.star.rdf.Statement) result.nextElement();
				XLiteral literal = UnoRuntime.queryInterface(XLiteral.class, stmt.Object);
				content = literal.getValue();
			}
			return content;
		}
		
		protected void saveQuery(Query query) throws Exception {
			XLiteral literal = Literal.create(componentContext, query.toString());
			XURI queryURI = URI.create(componentContext, NAMESPACE + "/" + query.getName());
			settings.addStatement(resource, queryURI, literal);
		}
		
		protected boolean hasQuery(String queryName) throws Exception {
			return this.getQueryProperties(queryName) != null;
		}
		
		protected Query getQuery(String queryName) throws Exception {
			return new Query(queryName);
		}
		
		protected void removeQuery(String queryName) throws Exception {
			XURI queryURI = URI.create(componentContext, NAMESPACE + "/" + queryName);
			settings.removeStatements(resource, queryURI, null);
		}
	}
	
	private class Query extends JSONObject {
	
		protected static final String QUERY_NODE_PREFIX = "Query-";
		protected static final String QUERY_PROPERTY = "query";
		protected static final String DRIVER_CLASS_PROPERTY = "driverClass";
		protected static final String URL_PROPERTY = "url";
		protected static final String USERNAME_PROPERTY = "username";
		protected static final String PASSWORD_PROPERTY = "password";
		protected static final String START_COLUMN_PROPERTY = "startColumn";
		protected static final String START_ROW_PROPERTY = "startRow";
		protected static final String ROW_COUNT_PROPERTY = "rowCount";
		protected static final String COLUMN_COUNT_PROPERTY = "columnCount";
		protected static final String SHEET_ID_PROPERTY = "sheetid";
		
		private String name;
	
		protected Query(String queryName) throws Exception {
			this.name = queryName;
			String content = settings.getQueryProperties(this.name);
			if(content == null)
				throw new Exception("Query '"+this.name+"' does not exists. Did you check its existence?");
			JSONObject temp = new JSONObject(content);
			String[] names = JSONObject.getNames(temp);
			for(int i=0; i<names.length; i++)
				this.put(names[i], temp.getString(names[i]));
		}
		
		protected Query(String query, String driverClass, String url, String username, 
				String password, int startColumn, int startRow,
				int rowCount, int columnCount) throws Exception {
			this.name = QUERY_NODE_PREFIX + UUID.randomUUID().toString();
			this.put(QUERY_PROPERTY, query);
			this.put(DRIVER_CLASS_PROPERTY, driverClass);
			this.put(URL_PROPERTY, url);
			this.put(USERNAME_PROPERTY, username);
			this.put(PASSWORD_PROPERTY, password);
			this.put(START_COLUMN_PROPERTY, ""+startColumn);
			this.put(START_ROW_PROPERTY, ""+startRow);
			this.put(COLUMN_COUNT_PROPERTY, ""+columnCount);
			this.put(ROW_COUNT_PROPERTY, ""+rowCount);
			this.put(SHEET_ID_PROPERTY, ""+getActiveSheetID());
		}
		
		protected Query(String query, String driverClass, String url, String username,  String password) throws Exception{
			this(query, driverClass, url, username, password, 0, 0, 0, 0);
		}
		
		public String getName(){
			return name;
		}
		
		public String getQuery(){
			return this.optString(QUERY_PROPERTY);
		}
		
		public String getDriverClass(){
			return this.optString(DRIVER_CLASS_PROPERTY);
		}
		
		public String getURL(){
			return this.optString(URL_PROPERTY);
		}
		
		public String getUsername(){
			return this.optString(USERNAME_PROPERTY);
		}
		
		public String getPassword(){
			return this.optString(PASSWORD_PROPERTY);
		}
		
		public int getStartColumn(){
			return Integer.parseInt(this.optString(START_COLUMN_PROPERTY, "0"));
		}
		
		public int getStartRow(){
			return Integer.parseInt(this.optString(START_ROW_PROPERTY, "0"));
		}
		
		public int getColumnCount(){
			return Integer.parseInt(this.optString(COLUMN_COUNT_PROPERTY, "0"));
		}
		
		public int getRowCount(){
			return Integer.parseInt(this.optString(ROW_COUNT_PROPERTY, "0"));
		}
		
		public String getSheetID(){
			return this.optString(SHEET_ID_PROPERTY, null);
		}
		
		public void setPositionAndArea(int startColumn, int startRow, int columnCount, int rowCount){
			this.put(START_COLUMN_PROPERTY, ""+startColumn);
			this.put(START_ROW_PROPERTY, ""+startRow);
			this.put(COLUMN_COUNT_PROPERTY, ""+columnCount);
			this.put(ROW_COUNT_PROPERTY, ""+rowCount);
		}
	}
}
