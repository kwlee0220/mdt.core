package mdt.aas.model;

import javax.annotation.Nullable;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public interface Referable {
	public @Nullable String getCategory();
	public void setCategory(String category);

	public @Nullable String getIdShort();
	public void setIdShort(String idShort);

	public LangStringSet getDisplayName();
	public void setDisplayName(LangStringSet displayName);

	public LangStringSet getDescription();
	public void setDescription(LangStringSet description);
	
	public @Nullable String getChecksum();
	public void setChecksum(String checksum);
}
