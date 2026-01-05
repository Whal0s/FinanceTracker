# My Personal Project

## A subtitle

A *bulleted* list:
- item 1
- item 2
- item 3

An example of text with **bold** and *italic* fonts.  

FORMATTING GUIDELINES ^
---

## OVERALL FUNCTIONALITY & PROJECT DESCRIPTION:
What will the application do?
I will make a business finance tracking application that will be able to store bills and deposits (financial entries). Later functionality will also include sotring accounting accruals and deferrals including prepaids expenses, unearned revenue, receivables and payables. Users can also mark financial entries as either planned or unplanned to showcase what portion of their overall inflows or outflows had been previously accounted for.

Who will use it?
- Anyone who wants to manage their finances in general can use this application.
- I will target an audience of students beginning to manage their incomes and cash outflows.


Why is this project of interest to you?
As a computer science and business combined major, I have been learning financial accounting which has been very new and interesting to me. I thought about the applications of how you might receive cash before after engaging in a business activity so I wanted to apply these concepts in my computer science learning.


## USER STORIES
- As a user, I want to be able to add a bill or deposit (financial tracker) into my personal tracker.
- As a user, I want to be able to view a list of all my deposits and bills.
- As a user, I want to be able to mark a financial entry as either planned or unplanned.
- As a user, I want to be able to delete or edit previous financial entries.
- As a user, I want to be able to see the amount each financial each entry either increased or decreased my overall balance by.
- As a user, I want to be able to save the entire state of my finance tracker app to file (if I so choose)
- As a user, I want to be able to be able to load a previous state of my finance app from file (if I so choose)



# Instructions for End User
- You can view the panel that displays the Entries that have already been added to the Users on right side of the interface.
- You can generate the first required action related to the user story "adding entries to a user in the app" by  clicking the desired user and pressing the "add entry" button.
- You can generate the second required action related to the user story "view a list of all deposits" by simply clicking your desired user and looking in the middle panel.
- You can generate the third required action related to the user story "to mark entries as planned or unplanned" by clicking the desired user and pressing the "add entry" button. You will be prompted to choose your planned state for the entry.
- You can generate the fifth required action related to the user story "see the amount each financial entry is increased or decreased the user's overal balance" via the entry panel in the middle.
- You can locate my visual component by looking to the right hand side to see a graph showcasing the total deposits / withdrawals.
- You can save the state of my application by pressing the save button on the bottom right.
- You can reload the state of my application by pressing the load button on the bottom right.

## PHASE 4 TASK 2: Changed
Thu Nov 27 14:44:21 PST 2025
Added deposit 'payday' for amount 3000 to user adam
Thu Nov 27 14:44:31 PST 2025
Added withdrawal 'car crash' for amount 500 to user adam
Thu Nov 27 14:44:43 PST 2025
Added withdrawal 'groceries' for amount 200 to user adam
Thu Nov 27 14:44:58 PST 2025
Added deposit 'deletable entry' for amount 1 to user adam
Thu Nov 27 14:45:05 PST 2025
Deleted deposit 'deletable entry' for amount 1 from user adam
Thu Nov 27 14:45:23 PST 2025
Added withdrawal 'payday' for amount 1000 to user jordan

Note: Event Logs only work with GUI Version of my financial app (not the old console based version as this was not specified to 
update in edX instructions).

## Phase 4: Task 3
Potential Refractoring Idea:3
If I were to refractor this project, I would potentially consider abstracting my entire financial app into its own "model" class. Currently users are stored in Finance Tracker GUI file part of the ui package. This means a lot of the user treatment and functionality is being done in the UI package which is unintuitve; so it would honestly make more sense to have abstracted the app into its own data type and have the ui file just make calls to the app. Additionally, this reduce the length of the very large Finance Tracker GUI file.

To complete this refractoring, I would create this new class which stores a list of users. I would have to create various public facing functions that the UI app could call. This would also potentially allow the possibility of multiple GUIs being opened as each one is dealing with their own app data in a very clean manner.


