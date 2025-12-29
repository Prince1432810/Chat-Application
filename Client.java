import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client implements ActionListener{
    static PrintWriter out;
    static JTextField text = new JTextField();
    JPanel msgArea = new JPanel();
    static Box vertical = Box.createVerticalBox();
    static JFrame frame = new JFrame();
    JPanel contactsPanel;
    int sidebarWidth;
    static BufferedReader userInput;
    static BufferedReader in;
    static JScrollPane msgScroll;
    static String name;

    Client(){
        GraphicsEnvironment ge[] = {GraphicsEnvironment.getLocalGraphicsEnvironment()};

        Rectangle full[] = {ge[0].getDefaultScreenDevice().getDefaultConfiguration().getBounds()};
        Rectangle usable[] = {ge[0].getMaximumWindowBounds()};

        int taskbarHeight[] = {full[0].height - usable[0].height};
        frame.setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int  width = screenSize.width;
        int height = screenSize.height;

        while (true) {
            name = JOptionPane.showInputDialog("Enter your name: ");

            // If user clicks Cancel
            if (name == null) {
                JOptionPane.showMessageDialog(null, "You cancelled!");
                continue;
            }
            name.trim();
            if(name.equalsIgnoreCase("Prince")){
                JOptionPane.showMessageDialog(null, "The name ‘Prince’ is reserved for administrative use. Kindly enter a different display name.");
                continue;
            }


            // Validation: allow only alphabets and spaces
            if (name.matches("[a-zA-Z0-9 ]+")) {
                break; // Valid name → exit loop
            } else {
                JOptionPane.showMessageDialog(null,
                        "Invalid input!\nPlease enter letters only (A–Z and 0-9).",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        // ------------------------------------------------------------------
        // Header and Brand name container

        JPanel header = new JPanel();
        header.setBackground(new Color(55, 55, 55));
        header.setLayout(null);
        int headerHeight = 50;
        header.setBounds(0, 0, width, headerHeight);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 40, 40)));
        frame.add(header);

        // ------------------------------------------------------------------
        // Brand name

        ImageIcon brand = new ImageIcon(ClassLoader.getSystemResource("icons/brand.png"));
        Image i2 = brand.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        brand = new ImageIcon(i2);
        JLabel img = new JLabel(brand);
        img.setBounds(20,(headerHeight - 40) / 2, 40, 40);
        header.add(img);


        JLabel name = new JLabel("onnectify");
        name.setFont(new Font("Gotham", Font.BOLD, 20));
        name.setForeground(new Color(0, 164, 221));
        int w = name.getPreferredSize().width;
        int h = name.getPreferredSize().height;
        name.setBounds(60, (headerHeight - h) / 2, w+3, h);
        header.add(name);

        int windowButton = 25;
        brand = new ImageIcon(ClassLoader.getSystemResource("icons/close.png"));
        i2 = brand.getImage().getScaledInstance(windowButton, windowButton, Image.SCALE_SMOOTH);
        brand = new ImageIcon(i2);
        img = new JLabel(brand);
        img.setBounds(width - windowButton,0, windowButton, windowButton);
        header.add(img);


        img.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.exit(0);  // exits the program
            }
        });

        brand = new ImageIcon(ClassLoader.getSystemResource("icons/max.png"));
        i2 = brand.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        brand = new ImageIcon(i2);
        img = new JLabel(brand);
        img.setBounds(width - windowButton*2-10,0, windowButton, windowButton);
        header.add(img);

        final boolean[] isMaximized = { true }; // your frame starts maximized
        final Rectangle[] prevBounds = { null };

        img.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (isMaximized[0]) {
                    // restore to previous size
                    prevBounds[0] = frame.getBounds();
                    frame.setExtendedState(JFrame.NORMAL);
                    frame.setBounds(prevBounds[0]);
                    isMaximized[0] = false;

                } else {
                    // maximize again
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    isMaximized[0] = true;
                }
            }
        });

        brand = new ImageIcon(ClassLoader.getSystemResource("icons/min.png"));
        i2 = brand.getImage().getScaledInstance(windowButton, windowButton, Image.SCALE_SMOOTH);
        brand = new ImageIcon(i2);
        img = new JLabel(brand);
        img.setBounds(width - windowButton*4,-8, windowButton, windowButton);
        header.add(img);

        img.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                frame.setState(JFrame.ICONIFIED);
            }
        });

        // ------------------------------------------------------------------
        // Full Right side Chat Page
        JPanel chatPage = new JPanel();
        chatPage.setBackground(new Color(240, 240, 240));
        chatPage.setLayout(null);
        int chatPageHeight = height - headerHeight;
        int chatPageWidth = width - 400;
        chatPage.setBounds(400, headerHeight, chatPageWidth, chatPageHeight);
        frame.add(chatPage);

        // ------------------------------------------------------------------
        // Header of Chat Page (Client's Information);
        JPanel chatHead = new JPanel();
        int chatHeadHeight = 60;
        chatHead.setBounds(0, 0, width-400, chatHeadHeight);
        chatHead.setBackground(Color.white);
        chatHead.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        chatHead.setLayout(null);
        chatPage.add(chatHead);


        // ------------------------------------------------------------------
        // Client's Photo
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/project.png"));
        i2 = i1.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        i1 = new ImageIcon(i2);
        img = new JLabel(i1);
        img.setBounds(20,(chatHeadHeight - 40) / 2, 40, 40);
        chatHead.add(img);

        name = new JLabel("Trinity");
        name.setFont(new Font("Arial", Font.BOLD, 16));
        name.setForeground(Color.BLACK);
        w = name.getPreferredSize().width;
        h = name.getPreferredSize().height;
        name.setBounds(80, (chatHeadHeight - h) / 2, w+3, h);
        chatHead.add(name);

        // ------------------------------------------------------------------
        // Video call icon
        ImageIcon video = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        int headIconSize = 20;
        int headIconGap = 10;
        i2 = video.getImage().getScaledInstance(headIconSize, headIconSize, Image.SCALE_SMOOTH);
        video = new ImageIcon(i2);
        img = new JLabel(video);
        img.setBounds(chatPageWidth-headIconSize*3-headIconGap*5,(chatHeadHeight - headIconSize) / 2, headIconSize, headIconSize);
        chatHead.add(img);

        // ------------------------------------------------------------------
        // Audio call icon
        ImageIcon call = new ImageIcon(ClassLoader.getSystemResource("icons/call.png"));
        i2 = call.getImage().getScaledInstance(headIconSize, headIconSize, Image.SCALE_SMOOTH);
        call = new ImageIcon(i2);
        img = new JLabel(call);
        img.setBounds(chatPageWidth-headIconSize*2-headIconGap*3,(chatHeadHeight - headIconSize) / 2, headIconSize, headIconSize);
        chatHead.add(img);

        // ------------------------------------------------------------------
        // Three dots icon
        ImageIcon dots = new ImageIcon(ClassLoader.getSystemResource("icons/dots.png"));
        i2 = dots.getImage().getScaledInstance(headIconSize, headIconSize, Image.SCALE_SMOOTH);
        dots = new ImageIcon(i2);
        img = new JLabel(dots);
        img.setBounds(chatPageWidth-headIconSize-headIconGap,(chatHeadHeight - headIconSize) / 2, headIconSize, headIconSize);
        chatHead.add(img);


        // --- earlier you created msgArea and msgScroll ---
        msgArea.setBounds(0, chatHeadHeight, chatPageWidth, chatPageHeight-chatHeadHeight-60-taskbarHeight[0]);
        msgArea.setBackground(new Color(240, 240, 240));

// IMPORTANT: set layout and add the vertical Box to msgArea here so it is part of the UI from start
        msgArea.setLayout(new BorderLayout());
        msgArea.add(vertical, BorderLayout.PAGE_START);
        vertical.setAlignmentY(Component.TOP_ALIGNMENT);


// create JScrollPane after attaching vertical
        msgScroll = new JScrollPane(msgArea);
        msgScroll.setBounds(0, chatHeadHeight, chatPageWidth, chatPageHeight - chatHeadHeight-60-taskbarHeight[0]);
        msgScroll.setBorder(null);
        msgScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        msgScroll.getVerticalScrollBar().setPreferredSize(new Dimension(1, 0));
        msgScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        msgScroll.getVerticalScrollBar().setUnitIncrement(15);
        chatPage.add(msgScroll);



        // ------------------------------------------------------------------
        // Full Text panel containing Emojy icon, File attatchment icon, input Field, Send button
        JPanel textPanel = new JPanel();
        int textPanelHeight = 60;
        textPanel.setBounds(0, chatPageHeight -textPanelHeight-taskbarHeight[0], chatPageWidth, textPanelHeight);
        textPanel.setBackground(Color.white);
        textPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        textPanel.setLayout(null);
        chatPage.add(textPanel);

        // ------------------------------------------------------------------
        // Emojy icon
        ImageIcon emoIcon = new ImageIcon(ClassLoader.getSystemResource("icons/face.png"));
        int iconHeight = 20;
        i2 = emoIcon.getImage().getScaledInstance(iconHeight, iconHeight, Image.SCALE_DEFAULT);
        emoIcon = new ImageIcon(i2);
        JLabel emo = new JLabel(emoIcon);
        int gap = 20;
        emo.setBounds(gap, (textPanelHeight-iconHeight)/2, iconHeight , iconHeight);
        textPanel.add(emo);

        // ------------------------------------------------------------------
        // File Attachment icon
        ImageIcon fileIcon = new ImageIcon(ClassLoader.getSystemResource("icons/files.png"));
        i2 = fileIcon.getImage().getScaledInstance(iconHeight, iconHeight, Image.SCALE_DEFAULT);
        fileIcon = new ImageIcon(i2);
        JLabel file = new JLabel(fileIcon);
        file.setBounds(gap*2+iconHeight, (textPanelHeight-iconHeight)/2, iconHeight , iconHeight);
        textPanel.add(file);

        // ------------------------------------------------------------------
        // Input from user

        int textWidth = chatPageWidth-(gap*7) - (iconHeight*4);
        text.setBounds(gap*3+iconHeight*2, (textPanelHeight-40)/2, textWidth, 40);
        text.setFont(new Font("Arial", Font.PLAIN, 16));
        text.addActionListener(e -> {
            sendMessage();
        });
        textPanel.add(text);

        // ------------------------------------------------------------------
        // Send button to send messages
        JButton send = new JButton("Send");
        send.setBounds(chatPageWidth-gap-90, (textPanelHeight-40)/2, 100, 40);
        send.setBackground(new Color(0, 130, 175));
        send.setForeground(Color.white);
        send.addActionListener(this);
        textPanel.add(send);

        // ------------------------------------------------------------------
        // Chat heading and Search Bar and write icon and filter icon

        JPanel panel = new JPanel();
        panel.setLayout(null);
        sidebarWidth = width - chatPageWidth;
        int sidebarHeight = 120;
        panel.setBackground(new Color(55, 55, 55));
        panel.setBounds(0, headerHeight, sidebarWidth, sidebarHeight);
        JLabel chatName = new JLabel("Chats");
        chatName.setFont(new Font("Roboto", Font.BOLD, 25));
        chatName.setForeground(Color.white);
        chatName.setBounds(20, 20, 200, 30);
        panel.add(chatName);

        int chatIconSize = 20;
        brand = new ImageIcon(ClassLoader.getSystemResource("icons/filter.png"));
        i2 = brand.getImage().getScaledInstance(chatIconSize, chatIconSize, Image.SCALE_SMOOTH);
        brand = new ImageIcon(i2);
        img = new JLabel(brand);
        img.setBounds(sidebarWidth - chatIconSize*2, chatIconSize+10, chatIconSize, chatIconSize);
        panel.add(img);

        brand = new ImageIcon(ClassLoader.getSystemResource("icons/edit.png"));
        i2 = brand.getImage().getScaledInstance(chatIconSize, chatIconSize, Image.SCALE_SMOOTH);
        brand = new ImageIcon(i2);
        img = new JLabel(brand);
        img.setBounds(sidebarWidth - chatIconSize*4, chatIconSize+10, chatIconSize, chatIconSize);
        panel.add(img);

        RoundedPanel search = new RoundedPanel(8);
        JLabel txt = new JLabel("Search");
        search.setLayout(null);
        txt.setBounds(10, 0, sidebarWidth-40, 30);
        txt.setOpaque(false);
        txt.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        txt.setFont(new Font("Roboto", Font.PLAIN, 15));
        txt.setForeground(new Color(150, 150, 150));
        search.setBackground(new Color(70, 70, 70));
        search.setBounds(20, sidebarHeight-20-30, sidebarWidth-40, 30);
        search.add(txt);
        panel.add(search);

        frame.add(panel);

        // ------------------------------------------------------------------
        // Contacts Panel

        contactsPanel = new JPanel();
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
        contactsPanel.setBackground(Color.gray);
        //Using a ScrollPane to make a Scrollable Panel for Contact lists.
        JScrollPane scroll = new JScrollPane(contactsPanel);
        scroll.setBounds(0, headerHeight + sidebarHeight, 400, height-headerHeight-panel.getHeight()-taskbarHeight[0]);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(1, 0));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        scroll.getVerticalScrollBar().setUnitIncrement(10);
        frame.add(scroll);

        ImageIcon img1 = new ImageIcon(ClassLoader.getSystemResource("icons/project.png"));
        addContact("Trinity", img1, "Sure, I will reach the campus on time.", "1:15 am");

        ImageIcon img2 = new ImageIcon(ClassLoader.getSystemResource("icons/myPic.png"));
        addContact("Prince", img2, "Practical timings for tommorow is 10am.", "10:59 AM");

        ImageIcon img3 = new ImageIcon(ClassLoader.getSystemResource("icons/league.png"));
        addContact("Mca league", img3, "Yeah sure, I wil copy that.", "11:12 AM");

        ImageIcon img4 = new ImageIcon(ClassLoader.getSystemResource("icons/brand.png"));
        addContact("Connectify", img4, "Now you can chat more securely with Connectify", "12:01 PM");

        ImageIcon img5 = new ImageIcon(ClassLoader.getSystemResource("icons/deepu.png"));
        addContact("Deepanshi MCA", img5, "We can buy two or three items.", "12:40 PM");

        ImageIcon img6 = new ImageIcon(ClassLoader.getSystemResource("icons/trio.png"));
        addContact("#TRIO", img6, "You have to reach on time.", "11:49 AM");

        ImageIcon img7 = new ImageIcon(ClassLoader.getSystemResource("icons/ammy.png"));
        addContact("Ammy MCA", img7, "So, do you practice DSA daily.", "yesterday");

        ImageIcon img8 = new ImageIcon(ClassLoader.getSystemResource("icons/toc.png"));
        addContact("THEORY OF COMPUTATION", img8, "Where is he now, we are getting late because of him.", "yesterday");

        ImageIcon img9 = new ImageIcon(ClassLoader.getSystemResource("icons/komal.png"));
        addContact("Komal MCA", img9, "Can you please make another project file for tomorrow.", "yesterday");








        // ------------------------------
        // JFrame Size
        // ------------------------------

        frame.setSize(width, height-taskbarHeight[0]);
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(new Color(55, 55, 55));
        frame.setUndecorated(true);
        frame.setVisible(true);
    }

    public void sendMessage(){
        String msg = text.getText();
        if(msg.isEmpty()) return;

        JPanel outputPanel = formatLabel(msg);

        // put the message on the right
        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.add(outputPanel, BorderLayout.LINE_END);

        // add to vertical (vertical is already in msgArea from constructor)
        SwingUtilities.invokeLater(() -> {
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(7));
            vertical.revalidate();
            vertical.repaint();

            // scroll to bottom
            SwingUtilities.invokeLater(() -> {
                JScrollBar sb = msgScroll.getVerticalScrollBar();
                sb.setValue(sb.getMaximum());
            });

        });

        text.setText("");

        out.println(name +": " + msg);
    }

    public void addContact(String name, ImageIcon icon, String newText, String msgTime){
        JPanel item = new JPanel();

        item.setLayout(null);
        item.setBackground(new Color(55, 55, 55));
        item.setPreferredSize(new Dimension(380, 70));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        Image i2 = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        icon = new ImageIcon(i2);
        JLabel img = new JLabel(icon);
        img.setBounds(20, 10, 45, 45);
        item.add(img);


        JLabel text = new JLabel(name);
        text.setForeground(Color.white);
        text.setFont(new Font("Arial", Font.BOLD, 15));
        text.setBounds(75, 15, 200, 20);
        item.add(text);

        JLabel text2 = new JLabel(newText);
        text2.setForeground(new Color(200, 200, 200));
        text2.setFont(new Font("Arial", Font.BOLD, 12));
        text2.setBounds(75, 35, 200, 20);
        item.add(text2);

        JLabel msgT = new JLabel(msgTime);
        int textWidth = msgT.getPreferredSize().width;
        msgT.setForeground(new Color(200, 200, 200));
        msgT.setFont(new Font("Arial", Font.BOLD, 12));
        msgT.setBounds(sidebarWidth-textWidth-20, 15, 200, 20);
        item.add(msgT);

        item.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){
                item.setBackground(new Color(70, 70, 70));
            }

            public void mouseExited(MouseEvent e){
                item.setBackground(new Color(55, 55, 55));
            }
        });

        contactsPanel.add(item);
//        contactsPanel.add(Box.createVerticalStrut(5));

        contactsPanel.revalidate();
        contactsPanel.repaint();
    }

    public void actionPerformed(ActionEvent e){
        sendMessage();
    }

    public static JPanel formatLabel(String out) {

        int maxWidth = 300;

        // TEXT (auto wrapping + fit-content)
        JTextArea text = new JTextArea(out);
        text.setFont(new Font("Tahoma", Font.PLAIN, 16));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setOpaque(false);
        text.setEditable(false);

        // Allow wrapping after 300px width
        text.setSize(new Dimension(maxWidth, Short.MAX_VALUE));

        RoundedPanel bubble = new RoundedPanel(40);

        // ROUNDED BUBBLE
        bubble.setLayout(new BorderLayout());
        bubble.setBackground(new Color(219, 242, 196));
        bubble.setBorder(new EmptyBorder(8, 12, 8, 12));

        bubble.add(text, BorderLayout.CENTER);

        // TIME LABEL
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

        JLabel time = new JLabel(sdf.format(cal.getTime()));
        time.setFont(new Font("Tahoma", Font.PLAIN, 10));
        time.setForeground(Color.GRAY);

        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.setOpaque(false);
        timePanel.add(time, BorderLayout.EAST);

        bubble.add(timePanel, BorderLayout.SOUTH);

        // OUTER PANEL (controls margin)
        JPanel outer = new JPanel();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setOpaque(false);

        // margin: top = 10, left = 50, bottom = 0, right = 10
        outer.setBorder(new EmptyBorder(5, 50, 0, 10));

        outer.add(bubble);

        return outer;
    }

    public static JPanel formatLabelLeft(String out) {

        int maxWidth = 300;

        // TEXT (auto wrapping + fit-content)
        JTextArea text = new JTextArea(out);
        text.setFont(new Font("Tahoma", Font.PLAIN, 16));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setOpaque(false);
        text.setEditable(false);
        text.setForeground(Color.black);

        // Allow wrapping after 300px width
        text.setSize(new Dimension(maxWidth, Short.MAX_VALUE));

        // ROUNDED BUBBLE
        RoundedPanel bubble = new RoundedPanel(40);
        bubble.setLayout(new BorderLayout());
//        bubble.setBackground(new Color(16, 90, 156));
        bubble.setBackground(new Color(189, 224, 243));
        bubble.setBorder(new EmptyBorder(8, 12, 8, 12));
        bubble.setForeground(Color.white);

        bubble.add(text, BorderLayout.CENTER);

        // TIME LABEL
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

        JLabel time = new JLabel(sdf.format(cal.getTime()));
        time.setFont(new Font("Tahoma", Font.PLAIN, 10));
        time.setForeground(Color.GRAY);

        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.setOpaque(false);
        timePanel.add(time, BorderLayout.EAST);

        bubble.add(timePanel, BorderLayout.SOUTH);

        // OUTER PANEL (controls margin)
        JPanel outer = new JPanel();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setOpaque(false);

        // margin: top = 10, left = 50, bottom = 0, right = 10
        outer.setBorder(new EmptyBorder(5, 50, 0, 10));

        outer.add(bubble);
        frame.validate();
        frame.repaint();
        return outer;
    }

    public static void main(String args[])throws Exception{
        Client ci = new Client();
        InetAddress ip = InetAddress.getLocalHost();
        Socket soc = new Socket(ip,8080);
        BufferedReader in ;

        out = new PrintWriter(soc.getOutputStream(), true);
        try{
            System.out.println("Client Started");

            userInput = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);

//             Thread to continuously receive messages from client
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {

                        // If this message was sent by this client, ignore it
                        if (msg.startsWith(name)) {
                            continue;  // don't show
                        }

                        JPanel panel = formatLabelLeft(msg);

                        JPanel left = new JPanel(new BorderLayout());
                        left.add(panel, BorderLayout.LINE_START);
                        vertical.add(left);
                        frame.validate();
                        SwingUtilities.invokeLater(() -> {
                            JScrollBar sb = msgScroll.getVerticalScrollBar();
                            sb.setValue(sb.getMaximum());
                        });

                        frame.validate();
                    }

                    frame.repaint();
                } catch (IOException e){
                    System.out.println("Connection closed.");
                }
            }).start();

            while(true){
                String str = userInput.readLine();
                out.println(str);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}

class RoundedPanel extends JPanel {

    private int radius;

    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}