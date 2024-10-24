package contactmanager.ui;

import contactmanager.model.Contact;
import contactmanager.service.ContactService;
import contactmanager.service.impl.ContactServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ContactManagerSwing extends JFrame {
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private ContactService contactService = new ContactServiceImpl();

    public ContactManagerSwing() {
        setTitle("个人通讯录系统");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel("姓名："));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("电话："));
        phoneField = new JTextField();
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("邮箱："));
        emailField = new JTextField();
        inputPanel.add(emailField);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("添加");
        addButton.addActionListener(new AddContactListener());
        buttonPanel.add(addButton);
        updateButton = new JButton("修改");
        updateButton.addActionListener(new UpdateContactListener());
        buttonPanel.add(updateButton);
        deleteButton = new JButton("删除");
        deleteButton.addActionListener(new DeleteContactListener());
        buttonPanel.add(deleteButton);

        String[] columnNames = {"ID", "姓名", "电话", "邮箱"};
        tableModel = new DefaultTableModel(columnNames, 0);
        contactTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(buttonPanel, BorderLayout.CENTER);
        contentPane.add(scrollPane, BorderLayout.SOUTH);

        loadContacts();
    }

    private void loadContacts() {
        tableModel.setRowCount(0);
        List<Contact> contacts = contactService.getAllContacts();
        for (Contact contact : contacts) {
            tableModel.addRow(new Object[]{contact.getId(), contact.getName(), contact.getPhone(), contact.getEmail()});
        }
    }

    private class AddContactListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            Contact contact = new Contact();
            contact.setName(name);
            contact.setPhone(phone);
            contact.setEmail(email);

            contactService.addContact(contact);
            loadContacts();
            clearFields();
        }
    }

    private class UpdateContactListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow!= -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Contact existingContact = contactService.getContactById(id);
                if (existingContact!= null) {
                    existingContact.setName(nameField.getText());
                    existingContact.setPhone(phoneField.getText());
                    existingContact.setEmail(emailField.getText());

                    contactService.updateContact(existingContact);
                    loadContacts();
                    clearFields();
                }
            } else {
                JOptionPane.showMessageDialog(null, "请选择要更新的联系人。");
            }
        }
    }

    private class DeleteContactListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow!= -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                contactService.deleteContact(id);
                loadContacts();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "请选择要删除的联系人。");
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ContactManagerSwing contactManager = new ContactManagerSwing();
            contactManager.setVisible(true);
        });
    }
}