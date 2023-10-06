package com.example.testikame.model;

import java.util.List;

public interface DatabaseHandlerI {
    public long addContact(ContactInfo contactInfo);
    public void addEmail(Email email);
    public void addPhone(PhoneNumber phoneNumber);
    public List<ContactInfo> getAllContact();
    public void update(int id, ContactInfo contactInfo);
    public ContactInfo getContactInfo(int id);
    public List<PhoneNumber> getPhoneNumberContact(int id);
    public List<Email> getEmailContact(int id);
}
