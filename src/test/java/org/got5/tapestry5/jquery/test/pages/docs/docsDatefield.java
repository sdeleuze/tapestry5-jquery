//
// Copyright 2010 GOT5 (GO Tapestry 5)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package org.got5.tapestry5.jquery.test.pages.docs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.Request;
import org.got5.tapestry5.jquery.utils.JQueryTabData;

public class docsDatefield
{
    @Property
    @Persist
    private int activeTabIndex;

    @Inject
    private Request request;

    @Persist
    private boolean afterFormSubmit;
    
    @Property
    private int blockId;
    
    @Property
    private Boolean useTabs;
   
    @Property
    private Boolean useMultiTabs;
	
    @Persist
	@Property
	private String activePanel;
	
	@Property
	private List<JQueryTabData> listTabData;
	
	
	void pageLoaded()
	{

		useTabs=true;
		useMultiTabs=true;		
		listTabData = new ArrayList<JQueryTabData>();
        listTabData.add(new JQueryTabData("DateField","TabsBlock1"));
        if(useMultiTabs)
        {	
        	listTabData.add(new JQueryTabData("I18N","TabsBlock2"));
        }
        listTabData.add(new JQueryTabData("Ajax Form","TabsBlock3"));
        listTabData.add(new JQueryTabData("Back to Prototype","TabsBlockLast"));
        
        if (_firstName == null && _lastName == null) {
			_firstName = "Humpty";
			_lastName = "Dumpty";
		}
	}
	/**simple page ****************************************************/
	@Persist
    @Property
    private Date date;
    
	/**I18N ****************************************************/
	@Persist
    private Date birthday;

    @Persist
    private Date asteroidImpact;
    
    @Component
	private Form testForm;
    
    @Component(id = "asteroidImpact")
	private DateField dfai;

    @Inject
    private PersistentLocale persistentLocale;

    @Validate("required")
    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public DateFormat getDateFormat()
    {
        return new SimpleDateFormat("MM/dd/yyyy");
    }

    @Validate("required")
    public Date getAsteroidImpact()
    {
        return asteroidImpact;
    }

    public void setAsteroidImpact(Date asteroidImpact)
    {
        this.asteroidImpact = asteroidImpact;
    }

    @OnEvent(value = EventConstants.SUCCESS, component="testForm")
	Object onSuccess()
	{
		Date now = new Date(); 
		if(asteroidImpact.before(now))
		{
			testForm.recordError(dfai, "must be later than now");			
		}	
		return null;
	 }
    
    
    void onActionFromClear()
    {
        birthday = null;
        asteroidImpact = null;
    }

    void onActionFromEnglish() { persistentLocale.set(Locale.ENGLISH); }

    void onActionFromFrench() { persistentLocale.set(Locale.FRENCH); }
    
    void onActionFromDeutsch() { persistentLocale.set(Locale.GERMAN); }
    
    /**AjaxForm ****************************************************/
    @Property
	private String _firstName;

	@Property
	private String _lastName;
	
	@Property
    private Date birthdayAjax;
	
	@Component
	private Form ajaxForm;
    
    @Component(id = "BirthdayAjax",parameters ={"validate=required"})
	private DateField df;
    
    @Component(id = "firstName")
	private TextField _firstNameField;
    
    @Component(id = "lastName")
	private TextField _lastNameField;

	@Component(id="nameZone")
	private Zone nameZone;
	
	@Component(id="formZone")
	private Zone formZone;
	
	
	
	@OnEvent(value = EventConstants.VALIDATE, component="ajaxForm")
	void onValidateAjaxForm() {
		
		if (_firstName == null || _firstName.trim().equals("")) {
			ajaxForm.recordError(_firstNameField, "First Name is required.");
		}
		if (_lastName == null || _lastName.trim().equals("")) {
			ajaxForm.recordError(_lastNameField, "Last Name is required.");
		}
		Date now = new Date(); 
		if(birthdayAjax.after(now))
		{
			ajaxForm.recordError(df, "invalid birthday");
		}
	}

	@OnEvent(value = EventConstants.SUCCESS, component="ajaxForm")
	Object onSuccessAjaxForm() {
		MultiZoneUpdate zoneUpdate =  new MultiZoneUpdate(nameZone).add(formZone);
		return zoneUpdate;
	}
	
	Object onFailure() {
		MultiZoneUpdate zoneUpdate =  new MultiZoneUpdate(formZone).add(nameZone);
		return zoneUpdate;
		
	}

	public String getName() {
		return _firstName + " " + _lastName;
	}
    
    
    
}
