package com.pepcus.appstudent.entity;

public class WrapperCoordinator {
	
	private Coordinator coordinator;
	
	public WrapperCoordinator(Coordinator coordinator) {
		this.coordinator =coordinator;
	}
	
	public Coordinator unwrapCoordinator() {
		return coordinator;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()){
			return false;
		}
		WrapperCoordinator wrapperCoordinator = (WrapperCoordinator) obj;
        return wrapperCoordinator.equals(coordinator.getFirstName().hashCode() + coordinator.getLastName().hashCode()
				+ coordinator.getDob().hashCode());
	}
	
	@Override
	public int hashCode() {
		return coordinator.getFirstName().hashCode() + coordinator.getLastName().hashCode()
				+ coordinator.getDob().hashCode();
	}

}
