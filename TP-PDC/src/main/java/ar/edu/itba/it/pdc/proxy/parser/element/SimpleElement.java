package ar.edu.itba.it.pdc.proxy.parser.element;

import java.util.LinkedList;
import java.util.List;

import ar.edu.itba.it.pdc.exception.InvalidProtocolException;
import ar.edu.itba.it.pdc.proxy.parser.element.util.ElemUtils;

public class SimpleElement extends XMPPElement {
	
	protected StartElement selement;
	private List<XMPPElement> body = new LinkedList<XMPPElement>();
	
	public SimpleElement(SimpleElement parent, StartElement selement) {
		super(parent);
		this.selement = selement;
	}

	@Override
	protected void appendDataToWrite(StringBuilder builder) {
		this.selement.appendDataToWrite(builder);
		for(XMPPElement e : this.body) {
			e.appendDataToWrite(builder);
		}
		builder.append("</" + this.selement.getName() + ">");
	}
	
	public void appendBody(XMPPElement e) {
		this.body.add(e);
	}
	
	public void appendEndElement(String name) {
		if(!this.selement.getName().equals(name)) {
			throw new InvalidProtocolException("Malformed stream");
		}
	}
	
	public String getLocalName() {
		return this.selement.getName().split(":")[0];
	}
	
	public String getName() {
		return this.selement.getName();
	}
	
	public StartElement getStartElement(){
		return this.selement;
	}
	
	public List<XMPPElement> getBody(){
		return this.body;
	}
	
	@Override
	public boolean isSimpleElement(){
		return true;
	}
	
	public boolean isStanza(){
		return false;
	}
	
	public List<SimpleElement> getChildren(String name) {
		List<SimpleElement> children = new LinkedList<SimpleElement>();
		for(XMPPElement e : this.getBody()) {
			if(e.isSimpleElement()) {
				if(ElemUtils.isElement(((SimpleElement) e), name)) {
					children.add((SimpleElement)e);
				}
			}
		}
		return children;
	}
	
	public SimpleElement getFirstChild(String name) {
		for(XMPPElement e : this.getBody()) {
			if(e.isSimpleElement()) {
				if(ElemUtils.isElement(((SimpleElement) e), name)) {
					return ((SimpleElement)e);
				}
			}
		}
		return null;
	}
	
	public boolean hasChild(String name) {
		for(XMPPElement e : this.getBody()) {
			if(e.isSimpleElement()) {
				if(ElemUtils.isElement(((SimpleElement) e), name)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String getFirstTextData() {
		for(XMPPElement e : this.getBody()) {
			if(e.isRawData()) {
				return e.getData();
			}
		}
		return "";
	}

}
