package com.hex.util;

import java.util.ArrayList;
import java.util.HashMap;

public class CodeGenerator {

    public CodeGenerator() {
    }

    public void generateOutput(ArrayList list) throws Exception {
        
       
            HashMap tableValues = (HashMap) list.get(0);
            String lsPresentation = (String) tableValues.get("PRESENTATION");
            String lsBusiness = (String) tableValues.get("BUSINESS");            
            String lsPersistence = (String) tableValues.get("PERSISTENCE");
            System.out.println("Presentation Layer"+lsPresentation);
            
            if("JSF".equalsIgnoreCase(lsPresentation)) {
                HexFacesGenerator jsfGen = new HexJSFGenerator();
                jsfGen.generateJsfPresentationTierFiles(list);
            }                 
            else if("IceFaces".equalsIgnoreCase(lsPresentation)) {
                HexFacesGenerator jsfGen = new HexIceFacesGenerator();
                jsfGen.generateJsfPresentationTierFiles(list);
            }
            else if("RichFaces".equalsIgnoreCase(lsPresentation)) {
                HexFacesGenerator jsfGen = new HexRichFacesGenerator();
                jsfGen.generateJsfPresentationTierFiles(list);
            }
            else if("PrimeFaces".equalsIgnoreCase(lsPresentation)) {
                HexFacesGenerator jsfGen = new HexPrimeFacesGenerator();
                jsfGen.generateJsfPresentationTierFiles(list);
            }
            if("Struts".equalsIgnoreCase(lsPresentation)) {
                HexStrutsGenerator strutsGen = new HexStrutsGenerator();
                strutsGen.generatePresentationTierFiles(list);
            }
            if("Struts2".equalsIgnoreCase(lsPresentation)) {
                HexStruts2Generator struts2Gen = new HexStruts2Generator();
                struts2Gen.generatePresentationTierFiles(list);
            }
            if("AngularJs 1.x".equalsIgnoreCase(lsPresentation)) {
            	HexAngularJs1Generator angularjs1Gen = new HexAngularJs1Generator();
            	angularjs1Gen.generatePresentationTierFiles(list);
            }
            if("Angular 2".equalsIgnoreCase(lsPresentation)) {
            	HexAngular2Generator angular2Gen = new HexAngular2Generator();
            	angular2Gen.generatePresentationTierFiles(list);
            }
            
            if ("Spring".equalsIgnoreCase(lsBusiness)) {
            	
            	if("AngularJs 1.x".equalsIgnoreCase(lsPresentation)) {
            		HexSpringServiceGenerator springServiceGen = new HexSpringServiceGenerator();
            		springServiceGen.generateServiceFiles(list);
                }
            	else
            	{
                HexSpringGenerator springGenerator = new HexSpringGenerator();
                springGenerator.generateSpringFiles(list);   
            	}
            }
            if("POJO".equalsIgnoreCase(lsBusiness)) {

            	if("AngularJs 1.x".equalsIgnoreCase(lsPresentation)) {
            		HexPojoServiceGenerator angularjs1ServiceGen = new HexPojoServiceGenerator();
            		angularjs1ServiceGen.generateServiceFiles(list);
                }
            	else
            	{
                HexPojoGenerator pojoGenerator = new HexPojoGenerator();
                pojoGenerator.generatePojoFiles(list);
            	}
            }  
            
            if ("Spring".equalsIgnoreCase(lsBusiness)) {
                if ("IBatis".equalsIgnoreCase(lsPersistence)) {
                    HexSpringIBATISGenerator loGenerator = new HexSpringIBATISGenerator();
                    loGenerator.generateIBatisFiles(list);
                }else if ("Hibernate".equalsIgnoreCase(lsPersistence)) {
                    HexSpringHibernateGenerator loGenerator = new HexSpringHibernateGenerator();
                    loGenerator.generateHibernateFiles(list);                    
                }
                else if (lsPersistence.startsWith("JPA")) {
                    HexSpringJpaGenerator loGenerator = new HexSpringJpaGenerator();
                    loGenerator.generateJpaFiles(list);
                }
            } 
            else if ("POJO".equalsIgnoreCase(lsBusiness)) {
                if ("Hibernate".equalsIgnoreCase(lsPersistence)) {
                    HexPojoHibernateGenerator loGenerator = new HexPojoHibernateGenerator();
                    loGenerator.generateHibernateFiles(list);
                } else if (lsPersistence.startsWith("JPA")) {
                    HexPojoJpaGenerator generator = new HexPojoJpaGenerator();
                    generator.generateJpaFiles(list);
                }
            }
            
        HexBuildGenerator buildGen = new HexBuildGenerator();
        String outDirectory = (String) tableValues.get("DIRECTORY");
        String warFile = (String) tableValues.get("WAR_FILE");
        String table = (String) tableValues.get("TABLE");            
        buildGen.generateBuildProperties(lsPresentation,lsBusiness,lsPersistence,outDirectory,warFile);
        list.clear();
        }
public void generateDeploy(ArrayList list, String deploy) throws Exception {
        
       
            HashMap tableValues = (HashMap) list.get(0);
            String lsPresentation = (String) tableValues.get("PRESENTATION");
            String lsBusiness = (String) tableValues.get("BUSINESS");            
            String lsPersistence = (String) tableValues.get("PERSISTENCE");
            System.out.println("Presentation Layer"+lsPresentation);
            
            if("JSF".equalsIgnoreCase(lsPresentation)) {
                HexFacesGenerator jsfGen = new HexJSFGenerator();
                jsfGen.generateJsfPresentationTierFiles(list);
            }else if("IceFaces".equalsIgnoreCase(lsPresentation)) {
                HexFacesGenerator jsfGen = new HexIceFacesGenerator();
                jsfGen.generateJsfPresentationTierFiles(list);
            }else if("RichFaces".equalsIgnoreCase(lsPresentation)) {
                HexFacesGenerator jsfGen = new HexRichFacesGenerator();
                jsfGen.generateJsfPresentationTierFiles(list);
            }
            else if("PrimeFaces".equalsIgnoreCase(lsPresentation)) {
                HexFacesGenerator jsfGen = new HexPrimeFacesGenerator();
                jsfGen.generateJsfPresentationTierFiles(list);
            }if("Struts".equalsIgnoreCase(lsPresentation)) {
                HexStrutsGenerator strutsGen = new HexStrutsGenerator();
                strutsGen.generatePresentationTierFiles(list);
            }
            if("Struts2".equalsIgnoreCase(lsPresentation)) {
                HexStruts2Generator struts2Gen = new HexStruts2Generator();
                struts2Gen.generatePresentationTierFiles(list);
            }
            if("AngularJs 1.x".equalsIgnoreCase(lsPresentation)) {
            	HexAngularJs1Generator angularjs1Gen = new HexAngularJs1Generator();
            	angularjs1Gen.generatePresentationTierFiles(list);
            }
            if("Angular 2".equalsIgnoreCase(lsPresentation)) {
            	HexAngular2Generator angular2Gen = new HexAngular2Generator();
            	angular2Gen.generatePresentationTierFiles(list);
            }
            if ("Spring".equalsIgnoreCase(lsBusiness)) {
            	if("AngularJs 1.x".equalsIgnoreCase(lsPresentation)) {
            		HexSpringServiceGenerator springServiceGen = new HexSpringServiceGenerator();
            		springServiceGen.generateServiceFiles(list);
                }
            	else
            	{
                HexSpringGenerator springGenerator = new HexSpringGenerator();
                springGenerator.generateSpringFiles(list);   
            	}              
            }
            if("POJO".equalsIgnoreCase(lsBusiness)) {
            	if("AngularJs 1.x".equalsIgnoreCase(lsPresentation)) {
            		HexPojoServiceGenerator angularjs1ServiceGen = new HexPojoServiceGenerator();
            		angularjs1ServiceGen.generateServiceFiles(list);
                }
            	else
            	{
                HexPojoGenerator pojoGenerator = new HexPojoGenerator();
                pojoGenerator.generatePojoFiles(list);
            	}
            }            
            if ("Spring".equalsIgnoreCase(lsBusiness)) {
                if ("IBatis".equalsIgnoreCase(lsPersistence)) {
                    HexSpringIBATISGenerator loGenerator = new HexSpringIBATISGenerator();
                    loGenerator.generateIBatisFiles(list);
                }else if ("Hibernate".equalsIgnoreCase(lsPersistence)) {
                    HexSpringHibernateGenerator loGenerator = new HexSpringHibernateGenerator();
                    loGenerator.generateHibernateFiles(list);                    
                }else if (lsPersistence.startsWith("JPA")) {
                    HexSpringJpaGenerator loGenerator = new HexSpringJpaGenerator();
                    loGenerator.generateJpaFiles(list);
                }
            } else if ("POJO".equalsIgnoreCase(lsBusiness)) {
                if ("Hibernate".equalsIgnoreCase(lsPersistence)) {
                    HexPojoHibernateGenerator loGenerator = new HexPojoHibernateGenerator();
                    loGenerator.generateHibernateFiles(list);
                } else if (lsPersistence.startsWith("JPA")) {
                    HexPojoJpaGenerator generator = new HexPojoJpaGenerator();
                    generator.generateJpaFiles(list);
                }
            }
            
        HexBuildGenerator buildGen = new HexBuildGenerator();
        String outDirectory = (String) tableValues.get("DIRECTORY");
        String warFile = (String) tableValues.get("WAR_FILE");
        String table = (String) tableValues.get("TABLE");   
        
        buildGen.generateBuildProperties(lsPresentation,lsBusiness,lsPersistence,outDirectory,warFile,deploy);
        list.clear();

        }
    }

