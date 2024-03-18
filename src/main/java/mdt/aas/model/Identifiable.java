package mdt.aas.model;

import javax.annotation.Nullable;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public interface Identifiable extends Referable {
	public String getId();
	public void setId(String id);

	public @Nullable AdministrativeInformation getAdministration();
	public void setAdministration(@Nullable AdministrativeInformation administration);
}
