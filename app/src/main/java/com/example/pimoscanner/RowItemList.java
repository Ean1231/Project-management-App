package com.example.pimoscanner;

import java.sql.Date;

public class RowItemList {

    //        private String dueDate;
        private String type;
        private  String status;
        private  String responsiblePerson;
        private String description;
        private String priority;
        private String workCenter;
        private Date dueDate;

        public RowItemList(String status, String responsiblePerson, String type, String description, String priority, String workCenter, Date dueDate ){
            this.status = status;
            this.responsiblePerson = responsiblePerson;
            this.type = type;
            this.description = description;
            this.priority = priority;
            this.workCenter = workCenter;
            this.dueDate = dueDate;

        }
    public String getPriority() {

        return priority;
    }

    public String getDescription(){
            return description;
    }

    public String getWorkCenter() {

        return workCenter;
    }
        public String getType() {

            return type;
        }

    public Date getDueDate() {
        return dueDate;
    }

    public String getStatus() {

            return status;
        }

        public String getResponsiblePerson() {

            return responsiblePerson;
        }
//        public String getDueDate(){
//            return dueDate;
//        }
}
