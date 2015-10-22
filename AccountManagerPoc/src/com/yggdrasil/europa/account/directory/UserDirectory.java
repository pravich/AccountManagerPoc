package com.yggdrasil.europa.account.directory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.NoSuchAttributeException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.yggdrasil.europa.account.config.Configuration;
import com.yggdrasil.europa.account.directory.exceptions.ConnectionException;
import com.yggdrasil.europa.account.directory.exceptions.InvalidCredentialException;
import com.yggdrasil.europa.account.directory.model.UserDirectoryEntity;

public class UserDirectory {
	
	DirContext dirctx = null;
	
	private static Logger logger = LogManager.getLogger(UserDirectory.class);
	
	UserDirectory(Hashtable<String, String> env) throws ConnectionException {
		try {
			dirctx = new InitialDirContext(env);
		} catch(CommunicationException excp){
			logger.error(excp.toString());
			logger.debug(excp, excp);
			
			throw new ConnectionException("InitialDirContext fail due to ConnectionException", excp.getCause());
		} catch(NamingException excp) {
			logger.error(excp.toString());
			logger.debug(excp, excp);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		try {
			if(dirctx != null) {
				dirctx.close();
			}
		} catch(NamingException excp) {
			logger.error(excp.toString());
			logger.debug(excp, excp);
		} finally {
			// finalize method in supper class is not automatically called by Java, like constructor.
			// need manually call it. 
			// http://javarevisited.blogspot.com/2012/03/finalize-method-in-java-tutorial.html
			super.finalize();
		}
	}
	
	public void close() {
		try {
			if(dirctx != null) {
				dirctx.close();
			}
		} catch(NamingException excp) {
			logger.error( excp.toString());
			logger.debug(excp, excp);
		}
	}
	
	public String getUserRole(String userId) {
		String base = "ou=user,dc=yggdrasil,dc=com";
		SearchControls sc = new SearchControls();
		sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		String filter = "(&(uid=" + userId + "))";
		
		String uid = null;
		String role = null;

		try {
			NamingEnumeration<SearchResult> results = dirctx.search(base, filter, sc);

			//while(results.hasMore()) { // <-- performance drop dramatically when exiting the loop

				SearchResult sr = (SearchResult)results.next();
				
				Attributes attrs = sr.getAttributes();
				
				uid = attrs.get("uid").get().toString();

				if(uid.equals(userId) && (attrs.get("title") != null)) {
					role = attrs.get("title").get().toString();

				}  
			//}
		} catch(NamingException excp) {
			logger.error( excp.toString());
			logger.debug(excp, excp);			
		}

		return(role);
	}
		
	public UserDirectoryEntity searchUser(String userId) {
		
		UserDirectoryEntity userEntity = null;
		
		String base = "ou=user,dc=yggdrasil,dc=com";
		
		try {
			SearchControls sc = new SearchControls();
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
			
			//String filter = "(&(objectClass=person)(uid=" + userId + "))";
			String filter = "(&(uid=" + userId + "))";
			NamingEnumeration<SearchResult> results = dirctx.search(base, filter, sc);
			
			//while(results.hasMore()) {
				SearchResult sr = (SearchResult) results.next();
				
				Attributes attrs = sr.getAttributes();
				
				/* Skip if not this user */
//				Attribute au = attrs.get("uid");
//				String uid = (String)au.get();
//				if(!uid.equals(username)) 
//					continue;
				/************************/
				
				userEntity = new UserDirectoryEntity();
				
				NamingEnumeration<? extends Attribute> ae = attrs.getAll();
				
				while(ae.hasMore()) {
					
					Attribute attr = (Attribute)ae.next();
					
					String name = attr.getID();
					NamingEnumeration<?> values = attr.getAll();
					while(values.hasMore()) {
						String value = values.next().toString();
						logger.debug("name=" + name + ", value=" + value);
						switch(name) {
							case "uid": userEntity.userId = value; break;
							case "cn": userEntity.username = value; break;
							case "userPassword": userEntity.password = value; break;
							case "mail": userEntity.email = value; break;
							case "gn": 
							case "givenName": userEntity.firstname = value; break;
							case "sn": userEntity.lastname = value; break;
							
							case "organizationname": 
							//case "o" : userEntity.company = value; break;
							case "title": userEntity.role = value; break;
							
							//case "mobile": userEntity.mobile = value; break;
							//case "homePhone": userEntity.homePhone = value; break;
							//case "telephoneNumber": userEntity.officePhone = value; break;
							//case "postalAddress": userEntity.officeAddress = value; break;
							//case "l": userEntity.city = value; break;
							//case "postalCode": userEntity.zipcode = value; break;
							
							case "description": userEntity.description = value; break;
						}
					}
				//}
			}		
		} catch(NullPointerException excp) {
			logger.error( excp.toString());
			logger.debug(excp, excp);
			return(null);
		}catch(Exception excp) {
			logger.error( excp.toString());
			logger.debug(excp, excp);
			return(null);
		}
		
		return(userEntity);
	}
	
	public List<String> getUserList() {
		String base = "ou=user,dc=yggdrasil,dc=com";
		List<String> usernameList = new ArrayList<String>();
		
		try {
			SearchControls sc = new SearchControls();
			sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
			sc.setCountLimit(0);
			
			String[] retAttrs = {"uid"};  
			sc.setReturningAttributes(retAttrs);
			
			NamingEnumeration<SearchResult> results = dirctx.search(base, "(&(uid=*))", sc);
			
			while(results.hasMore()) {
				SearchResult sr = (SearchResult) results.next();
				
				Attributes attrs = sr.getAttributes();
				Attribute au = attrs.get("uid");
				String uid = au.get().toString();
				usernameList.add(uid);
			}
		} catch(Exception excp) {
			logger.error( excp.toString());
			logger.debug(excp, excp);
			return(null);
		}
		return(usernameList);
	}
	
	public static boolean signon(String userId, String password) throws ConnectionException, InvalidCredentialException {
		
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, Configuration.dirProviderUrl);
		env.put(Context.SECURITY_AUTHENTICATION, Configuration.dirSecurityAuthentication);
		env.put(Context.SECURITY_PRINCIPAL, "uid="+ userId + ",ou=user,dc=yggdrasil,dc=com");
		env.put(Context.SECURITY_CREDENTIALS, password);

		DirContext dc = null;
		boolean success = false;
		
		try {
			// This line below show be enough for verifying against LDAP
			dc = new InitialDirContext(env);
			success = true;
			
			logger.debug("user=" + userId + " sign on succeeds");
		} catch(CommunicationException excp) {
			logger.error( excp.getCause());
			logger.debug(excp, excp);
			
			throw new ConnectionException("InitialDirContext fail due to ConnectionException", excp.getCause());
		} catch(AuthenticationException excp) {
			logger.debug("user=" + userId + " sign on fails");
			
			throw new InvalidCredentialException("invalid username or password.");
		} catch(NamingException excp) {
			logger.error( excp.toString());
			logger.debug(excp, excp);
		}
		
		if(dc != null) {
			try {
				dc.close();
			} catch(NamingException excp) {
				logger.error( excp.toString());
				logger.debug(excp, excp);
			}
		}
		
		return(success);
	}
	
	public boolean createUser(UserDirectoryEntity user) {
		
		try {
			Attributes attrs = new BasicAttributes(true);
			
			attrs.put(new BasicAttribute("objectClass", "top"));
			attrs.put(new BasicAttribute("objectClass", "person"));
			attrs.put(new BasicAttribute("objectClass", "organizationPerson"));
			attrs.put(new BasicAttribute("objectClass", "organizationUnit"));
			attrs.put(new BasicAttribute("objectClass", "inetOrgPerson"));
			// Mandatory attributes
			attrs.put( new BasicAttribute("uid", user.userId));
			attrs.put( new BasicAttribute("cn", user.username));
			attrs.put( new BasicAttribute("userPassword", user.password));			
			attrs.put( new BasicAttribute("mail", user.email));
			attrs.put( new BasicAttribute("gn", user.firstname));
			attrs.put( new BasicAttribute("sn", user.lastname));
			
			// Option attributes
//			if(user.company != null)
//				attrs.put( new BasicAttribute("organizationName", user.company));
			
			if(user.role != null)
				attrs.put( new BasicAttribute("title", user.role));
			
//			if(user.mobile != null)
//				attrs.put( new BasicAttribute("mobile", user.mobile));
			
//			if(user.homePhone != null)
//				attrs.put( new BasicAttribute("homephone", user.homePhone));
			
//			if(user.officePhone != null)
//				attrs.put( new BasicAttribute("telephoneNumber", user.officePhone));
			
//			if(user.officeAddress != null)
//				attrs.put( new BasicAttribute("postalAddress", user.officeAddress));
			
//			if(user.city != null)
//				attrs.put( new BasicAttribute("l", user.city));

//			if(user.zipcode != null)
//				attrs.put( new BasicAttribute("postalCode", user.zipcode));
			
			if(user.description != null)
				attrs.put( new BasicAttribute("description", user.description));
			
			String dn = "uid=" + user.userId + ",ou=user,dc=yggdrasil,dc=com";
			
			dirctx.bind(dn, dirctx, attrs);
			
			logger.debug("user[" + user.userId + "] successfully created.");
		} catch(NamingException excp) {
			logger.error(excp.toString());
			logger.debug(excp, excp);
			return(false);
		} catch(Exception excp) {
			logger.error(excp.toString());
			logger.debug(excp, excp);
			return(false);
		}
		return(true);
	}
	
	public boolean deleteUser(String userId) {
		
		try {
			dirctx.destroySubcontext("uid=" + userId + ",ou=user,dc=yggdrasil,dc=com");
			
			logger.info("delete user=" + userId + " successfully");
		} catch(Exception excp) {
			logger.error(excp.toString());
			logger.debug(excp, excp);
			return(false);
		}
		
		return(true);
	}
	
	public UserDirectoryEntity modifyUserAttributes(String userId, Hashtable<String, String> attributes) {
		
		UserDirectoryEntity ue = searchUser(userId);
		if(ue == null) {
			logger.debug("invalid user=" + userId);
			return(null);
		}
		
		ArrayList<ModificationItem> modificationList = new ArrayList<ModificationItem>();
		
		for(String attr : attributes.keySet()) {
			
			String value = attributes.get(attr);
			
			logger.debug("attribute=" + attr + ", value=" + value);
			
			switch(attr) {
			case "username": 
				if(ue.username == null) {
					Attribute modAttr = new BasicAttribute("cn", value);
					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
					continue;
				} else if (!ue.username.equals(value)) {
					Attribute modAttr = new BasicAttribute("cn", value);	
					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
					continue;
				}
				break;
			case "email": 
				if(ue.email == null) {
					Attribute modAttr = new BasicAttribute("mail", value);
					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
					continue;
				} else if (!ue.email.equals(value)) {
					Attribute modAttr = new BasicAttribute("mail", value);	
					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
					continue;
				}
				break;
			case "firstname":
				if(ue.firstname == null) {
					Attribute modAttr = new BasicAttribute("gn", value);
					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
					continue;
				} else if(!ue.firstname.equals(value)) {
					Attribute modAttr = new BasicAttribute("gn", value);	
					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
					continue;
				}
				break;
			case "lastname":
				if(ue.lastname == null) {
					Attribute modAttr = new BasicAttribute("sn", value);
					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
					continue;
				} else if(!ue.lastname.equals(value)) {
					Attribute modAttr = new BasicAttribute("sn", value);	
					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
					continue;
				}
				break;	
//			case "company":
//				if(ue.company == null) {
//					Attribute modAttr = new BasicAttribute("organizationName", value);
//					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
//					continue;
//				} else if(!ue.company.equals(value)) {
//					Attribute modAttr = new BasicAttribute("organizationName", value);	
//					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
//					continue;
//				}
//				break;
			case "role":
				if(ue.role == null) {
					Attribute modAttr = new BasicAttribute("title", value);
					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
					continue;
				} else if(!ue.role.equals(value)) {
					Attribute modAttr = new BasicAttribute("title", value);	
					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
					continue;
				}
				break;
//			case "mobile":
//				if(ue.mobile == null) {
//					Attribute modAttr = new BasicAttribute("mobile", value);
//					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
//					continue;
//				} else if(!ue.mobile.equals(value)) {
//					Attribute modAttr = new BasicAttribute("mobile", value);	
//					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
//					continue;
//				}
//				break;
//			case "homePhone":
//				if(ue.homePhone == null) {
//					Attribute modAttr = new BasicAttribute("homePhone", value);
//					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
//					continue;
//				} else if(!ue.homePhone.equals(value)) {
//					Attribute modAttr = new BasicAttribute("homePhone", value);	
//					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
//					continue;
//				}
//				break;
//			case "officePhone":
//				if(ue.officePhone == null) {
//					Attribute modAttr = new BasicAttribute("telephoneNumber", value);
//					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
//					continue;
//				} else if(!ue.officePhone.equals(value)) {
//					Attribute modAttr = new BasicAttribute("telephoneNumber", value);	
//					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
//					continue;
//				}
//				break;
//			case "officeAddress":
//				if(ue.officeAddress == null) {
//					Attribute modAttr = new BasicAttribute("postalAddress", value);
//					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
//					continue;
//				} else if(!ue.officeAddress.equals(value)) {
//					Attribute modAttr = new BasicAttribute("postalAddress", value);	
//					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
//					continue;
//				}
//				break;
//			case "city":
//				if(ue.city == null) {
//					Attribute modAttr = new BasicAttribute("l", value);
//					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
//					continue;
//				} else if(!ue.city.equals(value)) {
//					Attribute modAttr = new BasicAttribute("l", value);	
//					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
//					continue;
//				}
//				break;
//			case "zipcode":
//				if(ue.zipcode == null) {
//					Attribute modAttr = new BasicAttribute("postalCode", value);
//					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
//					continue;
//				} else if(!ue.zipcode.equals(value)) {
//					Attribute modAttr = new BasicAttribute("postalCode", value);	
//					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
//					continue;
//				}
//				break;
			case "description":
				if(ue.description == null) {
					Attribute modAttr = new BasicAttribute("description", value);
					modificationList.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, modAttr));
					continue;
				} else if(!ue.description.equals(value)) {
					Attribute modAttr = new BasicAttribute("description", value);	
					modificationList.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modAttr));
					continue;
				}
				break;
			}
		}
		
		ModificationItem[] mods = modificationList.toArray(new ModificationItem[modificationList.size()]);
		
		try {
			dirctx.modifyAttributes("uid=" + userId + ",ou=user,dc=yggdrasil,dc=com", mods);
			logger.debug("attribute modification succeeds.");
		} catch(NamingException excp) {
			logger.error(excp.toString());
			logger.debug(excp, excp);
			return(null);
		}

		return(searchUser(userId));
	}
	
	public boolean changePassword(String userId, String oldPassword, String newPassword) throws ConnectionException, InvalidCredentialException {
		if(signon(userId, oldPassword)) {
			ModificationItem[] mods = new ModificationItem[1];
			Attribute modPassword = new BasicAttribute("userPassword", newPassword);
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, modPassword);
			try {
				dirctx.modifyAttributes("uid=" + userId + ",ou=user,dc=yggdrasil,dc=com", mods);
				logger.debug("password is changed successfully.");
			} catch(NamingException excp) {
				logger.error(excp.toString());
				logger.debug(excp, excp);
				return(false);
			}
			return(true);
		} else {
			logger.debug("password change failed.");
			return(false);
		}
	}
	
	public boolean deleteUserAttribute(String userId, String attribute) {
		String ldapAttrName = null;
		switch(attribute) {
		case "firstname"     : ldapAttrName = "gn"; break;
		case "lastname"      : ldapAttrName = "sn"; break;
//		case "company"       : ldapAttrName = "o"; break;
		case "role"          : ldapAttrName = "title"; break;
//		case "mobile"        : ldapAttrName = "mobile"; break;
//		case "homePhone"     : ldapAttrName = "homePhone"; break;
//		case "officePhone"   : ldapAttrName = "telephoneNumber"; break;
//		case "officeAddress" : ldapAttrName = "postalAddress"; break;
//		case "city"          : ldapAttrName = "l"; break;
//		case "zipcode"       : ldapAttrName = "postalCode"; break;
		case "description"   : ldapAttrName = "description"; break;
		default: 
			logger.debug("invalid or not allowed deletion attribute [" + attribute + "].");
			return(false);
		}
		
		ModificationItem[] mods = new ModificationItem[1];
		Attribute delAttr = new BasicAttribute(ldapAttrName);
		mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, delAttr);	
		
		try {
			dirctx.modifyAttributes("uid=" + userId + ",ou=user,dc=yggdrasil,dc=com", mods);
			logger.debug("attribute " + attribute + " is deleted successfully.");
		} catch(NoSuchAttributeException excp) {
			logger.debug("no attribute " + attribute);
			return(false);
		} catch(NamingException excp) {
			logger.error(excp.toString());
			logger.debug(excp, excp);
			return(false);
		}
		return(true);
	}
}
