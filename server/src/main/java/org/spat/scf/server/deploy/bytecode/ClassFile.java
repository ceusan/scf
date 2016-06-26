package org.spat.scf.server.deploy.bytecode;

/**
 * 
 * @author Service Platform Architecture Team 
 * 
 * 
 */
public class ClassFile {
	
	private String clsName;
	private byte[] clsByte;
	
	
	public ClassFile(String clsName, byte[] clsByte){
		this.setClsName(clsName);
		this.setClsByte(clsByte);
	}
	
	
	public String getClsName() {
		return clsName;
	}
	public void setClsName(String clsName) {
		this.clsName = clsName;
	}
	public byte[] getClsByte() {
		return clsByte;
	}
	public void setClsByte(byte[] clsByte) {
		this.clsByte = clsByte;
	}
}
