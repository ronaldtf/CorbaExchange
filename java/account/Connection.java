package account;

import java.nio.channels.AlreadyBoundException;
import java.util.Properties;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManager;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

public class Connection {

	private ORB orb = null;
	private static Connection instance = null;
	private static final String CONF_NAME = "conf/server.cfg";
	private Properties properties;
	private POA poa;
	
	private Connection() {
	}
	
	public static Connection getInstance() throws Exception {
		if (instance == null) {
			instance = new Connection();
			init();
		}
		return instance;
	}
	
	private static void init() throws Exception {
		instance.properties = Utils.readProperties(CONF_NAME);
		String args[] = { "-ORBInitRef", "NameService=corbaname::" + 
				instance.properties.getProperty("org.omg.CORBA.ORBInitialHost") + ":" + 
				instance.properties.getProperty("org.omg.CORBA.ORBInitialPort")};
		System.out.println("ARGS: " + args[0] + " " + args[1]);
		instance.orb = ORB.init(args, instance.properties);
		
		Object obj = instance.orb.resolve_initial_references("RootPOA");
		instance.poa = POAHelper.narrow(obj);
	}
	
	public void bindObjectToName(Object objRef, String componentName, String contextName, String objectType) throws Exception {
		NamingContext namingContext;
		NamingContext subContext;
		try {
			Object object = orb.resolve_initial_references("NameService");
			namingContext = NamingContextHelper.narrow(object);
			if (namingContext == null)
				throw new Exception("A problem occurred when narrowing the naming context");
		} catch (Exception e) {
			throw new Exception("Name service does not exist");
		}
		
		try {
			NameComponent nameComponent[] = new NameComponent[] {
					new NameComponent(componentName, contextName)
			};
			try {
				subContext = namingContext.bind_new_context(nameComponent);
			} catch (NotFound nf) {
				throw new Exception("Cannot bind new context");
			} catch (AlreadyBoundException abe) {
				//	throw new Exception("Context is already bound");
				Object object = namingContext.resolve(nameComponent);
				if ((subContext = NamingContextHelper.narrow(object)) == null)
					throw new Exception("Cannot rebind the naming context");
			}
			NameComponent objectName[] = new NameComponent[] {
					new NameComponent(objectType, "Object")
			};
			try {
				subContext.bind(objectName, objRef);
			} catch (AlreadyBound ab) {
				// throw new Exception("Object already bound to the subcontext");
				subContext.rebind(objectName, objRef);
			}
		} catch (COMM_FAILURE cf) {
			throw new Exception("A problem has occurred when contacting the naming service");
		} catch (SystemException se) {
			throw new Exception("A problem has occurred whe using the naming service");
		}
	}	
	
	
	public Object activate(Servant obj) throws ServantAlreadyActive, WrongPolicy, ServantNotActive {
		poa.activate_object(obj);
		return poa.servant_to_reference(obj);
	}
	
	public void run() throws AdapterInactive {
		POAManager pman = poa.the_POAManager();
		pman.activate();

		orb.run();
	}
}
