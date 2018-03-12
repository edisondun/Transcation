package transaction;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import javax.persistence.TypedQuery;

public class TransactionMgmtDAO {
	
    protected static SessionFactory sessionFactory;
    private static final boolean MOCK = true;
    
    protected void setup() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure()
				.build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception ex) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
    }
 
    protected void exit() {
		sessionFactory.close();
    }
 
    protected SessionFactory getSessionFactory() {
    		if(sessionFactory == null) {
    			setup();
    		}
    		return sessionFactory;
    }
    
    
    protected List<OrderAddress> getOrderAddress(long addressId) {
    		
		Session session = getSessionFactory().openSession();		
	    	String hql = "FROM OrderAddress E WHERE E.addressId = :addressId";
	    	TypedQuery<OrderAddress>  query = session.createQuery(hql,OrderAddress.class);
	    	query.setParameter("addressId",addressId);
	    	List<OrderAddress> results = query.getResultList();
	    	
	    	return results;
    }
 
    protected UserAddress getUserAddress(long userId) {
		
		Session session = getSessionFactory().openSession();		
	    	String hql = "FROM UserAddress E WHERE E.id = (select max(rec.id) from UserAddress rec where rec.userId = :userId)";
	    	TypedQuery<UserAddress>  query = session.createQuery(hql,UserAddress.class);
	    	query.setParameter("userId",userId);
	    	List<UserAddress> results = query.getResultList();
	    	if(results == null || results.isEmpty())return null;
	    	return results.get(0);
    }
    
    protected boolean mockWareHouseAPI(boolean success) {
    		return success;
    }
 
    protected void updateAddress(UserAddress address) {
    		if(address == null) return;
		Session session = getSessionFactory().openSession();		
		Transaction tx = null;    		
		
		try {
			tx = session.beginTransaction();
			UserAddress userOldAddr = getUserAddress(address.getUserId());
			session.save(address);
			if(userOldAddr != null) {
				List<OrderAddress> orderAddress = getOrderAddress(userOldAddr.getAddressId());
				for(OrderAddress addr: orderAddress) {
					addr.setAddressId(address.getAddressId());
		    			Date date = new Date(System.currentTimeMillis());
		    			Timestamp sqlTimestamp = new java.sql.Timestamp(date.getTime());
		    			addr.setModifiedDate(sqlTimestamp);
		    			session.update(addr);
				}
				if(!mockWareHouseAPI(MOCK)) {
					throw new Exception("WareHouseAPI call fail!");
				}
			}
			tx.commit();
		}

		catch (Exception e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		} finally {
			session.close();
		}
    		 	
    }
    
 
	public static void main(String[] args) {
		TransactionMgmtDAO manager = new TransactionMgmtDAO();
		
		UserAddress address = new UserAddress();
		address.setUserId(12345);
		address.setFirstName("Edison");
		address.setLastName("Wong");
		address.setLine1("10779 Alderbrook LN");
		address.setState("CA");
		address.setCountry("US");
		Date date = new Date(System.currentTimeMillis());
		Timestamp sqlTimestamp = new java.sql.Timestamp(date.getTime());
		address.setCreatedDate(sqlTimestamp);
		
		manager.updateAddress(address);		
		manager.exit();
	}

}
