/**
 * 
 */
package com.xiugou.x1.battle.buff.status;

import java.util.HashSet;
import java.util.Set;

/**
 * @author YY
 *
 */
public enum StatusEnum {
    NORMAL("正常") {
    	public AbstractStatus create() {
    		return null;
    	}
    },
    
    FREEZED("冰冻") {
    	public AbstractStatus create() {
    		return new StunStatus();
    	}
    },
    
    STUN("眩晕") {
    	public AbstractStatus create() {
    		return new StunStatus();
    	}
    },
    
    FLOATING("浮空") {
    	public AbstractStatus create() {
    		return new FloatingStatus();
    	}
		@Override
		protected void unReachable(Set<StatusEnum> unReachableStatus) {
			unReachableStatus.add(FLOATING);
		}
    },
    
    TAUNT("嘲讽") {
    	public AbstractStatus create() {
    		return new TauntStatus();
    	}
    	@Override
		protected void unReachable(Set<StatusEnum> unReachableStatus) {
			unReachableStatus.add(TAUNT);
		}
    },
    
    INVICTUS("无敌") {
    	public AbstractStatus create() {
    		return new InvictusStatus();
    	}
    	protected void unReachable(Set<StatusEnum> unReachableStatus) {
            unReachableStatus.add(FREEZED);
            unReachableStatus.add(STUN);
            unReachableStatus.add(FLOATING);
            unReachableStatus.add(TAUNT);
            unReachableStatus.add(FEARLESS);
        }
    },
    
    FEARLESS("霸体") {
    	public AbstractStatus create() {
    		return new FearlessStatus();
    	}
    	protected void unReachable(Set<StatusEnum> unReachableStatus) {
    		unReachableStatus.add(FREEZED);
            unReachableStatus.add(STUN);
            unReachableStatus.add(FLOATING);
            unReachableStatus.add(TAUNT);
        }
    },
    
    ;
	
	static {
		for(StatusEnum statusEnum : StatusEnum.values()) {
			statusEnum.unReachable(statusEnum.unReachableStatus);
		}
	}
	
    private final String name;
    private final Set<StatusEnum> unReachableStatus = new HashSet<>();
    
    private StatusEnum(String name) {
        this.name = name;
    }
	
	protected void unReachable(Set<StatusEnum> unReachableStatus) {
		//该状态下不能达到的状态集
	}
	
	public abstract AbstractStatus create();

	public boolean canReachStatus(StatusEnum status) {
		return !unReachableStatus.contains(status);
	}

	public String getName() {
		return name;
	}
	
	
	
}
