package transaction;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "order_address")
public class OrderAddress {
	
    @Id
    @Column(name = "line_id")
    private long lineId;
    
    @Column(name = "order_id")
    private long orderId;
    
    @Column(name = "address_id")
    private long addressId;

    @Column(name = "created_date")
    private Timestamp createdDate;
    
    @Column(name = "modified_date")
    private Timestamp modifiedDate;

	public long getLineId() {
		return lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = lineId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public String toString() {
		return "OrderAddress [lineId=" + lineId + ", orderId=" + orderId + ", addressId=" + addressId + ", createdDate="
				+ createdDate + ", modifiedDate=" + modifiedDate + "]";
	}
    
    

}
