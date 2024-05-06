/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/register")
@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB


public class register extends HttpServlet {

    private String dbURL = "jdbc:mysql://localhost:3306/robust";
    private String dbUser = "root";
    private String dbPass = "root";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
       response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String userid = request.getParameter("name");
        String pass = request.getParameter("pwd");
        String dob = request.getParameter("dob");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mob");
        String location = request.getParameter("cap");
        String utype = request.getParameter("utype");
        String stype = request.getParameter("stype");
        
        String t1 = request.getParameter("t1");
        
        
         
        InputStream inputStream = null; // input stream of the upload file
         
        // obtains the upload file part in this multipart request
        Part filePart = request.getPart("photo");
        if (filePart != null) {
            // prints out some information for debugging
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());
             
            // obtains input stream of the upload file
            inputStream = filePart.getInputStream();
        }
         
        Connection conn = null; // connection to the database
        String message = null;  // message will be sent back to client
         
        try {
            // connects to the database
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
 
            // constructs SQL statement
            
            String query1="select * from user  where uname='"+userid+"' or email='"+email+"' or mobile='"+mobile+"' "; 
	    Statement st1=conn.createStatement();
	    ResultSet rs1=st1.executeQuery(query1);
            
            if(!rs1.next()){
            String sql = "insert into user(uname,pwd,dob,email,mobile,location,utype,stype,cname,imagess,count,sk) values(?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userid);
            statement.setString(2, pass);
            statement.setString(3, dob);
            statement.setString(4, email);
            statement.setString(5, mobile);
            statement.setString(6, location);
            statement.setString(7, utype);
            
            statement.setString(8, stype);
            statement.setString(9, t1);
            
            
             
            if (inputStream != null) {
                // fetches input stream of the upload file for the blob column
                statement.setBlob(10, inputStream);
            }
                       int rank=0;
			String as="Rejected";
             statement.setInt(11,rank );
             statement.setString(12, as);
 
            // sends the statement to the database server
            int row = statement.executeUpdate();
            if (row == 1) {
                
                response.sendRedirect("register.html?Registration_done_success");
                
            }
            }
            else
            {
               response.sendRedirect("index.html?Registration_failed");
            }
        } catch (SQLException ex) {
            
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                // closes the database connection
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(register.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
