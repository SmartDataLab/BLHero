/**
 * 
 */
package org.gaming.prefab.clan;

/**
 * @author YY
 *
 */
public abstract class ClanAbstractController<C extends IClan, M extends IClanMember> {

	public C found(long leaderId) {
		return null;
	}
	
	public void apply(long applicantId, long clanId) {
		
	}
	
	public void allowApply(long managerId, long applicantId) {
		
	}
	
	public void rejectApply(long managerId, long applicantId) {
		
	}
	
	public void quickJoin(long applicantId, long clanId) {
		
	}
	
	public void quit(long memberId) {
		
	}
	
	
}
