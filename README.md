KvizApp

KvizApp is an Android application that gives students a clear and practical way to access university quizzes, manage course enrolments and follow their results. The app connects to an existing faculty service and organizes the available data into a focused mobile experience built with Jetpack Compose.

The project was developed as an individual assignment for the Mobile Application Development course at the Faculty of Electrical Engineering, University of Sarajevo.

Main features

Account access through a student hash

Course and group enrolment

Quiz filtering and search by quiz or course name

Access checks based on the student's enrolled course and group

Guided quiz flow with answer locking and progress tracking

Result and score overview for completed quizzes

Profile statistics for available, active and completed quizzes

Automatic refresh, pull-to-refresh and clear recovery options when loading fails

How the application works

After signing in, the student can enrol in a course and select the appropriate group. The quiz list then provides filtering and search tools for finding relevant quizzes. Before a quiz is opened, the application checks whether the student belongs to the required course and group.

During a quiz, each submitted answer is locked to prevent accidental changes, while the progress indicator shows the current position. Once all questions are completed, the result screen presents the achieved score. Completed quizzes remain available for reviewing the result but cannot be submitted again.

Architecture

KvizApp follows an MVVM-based structure with a separate domain layer. UI state is exposed through StateFlow, network operations are handled asynchronously, and business rules remain outside the Compose screens.

Layer

Responsibility

data/models

Domain and API data models such as quizzes, courses, groups, questions and results

data/repositories

Communication with the faculty backend and data access operations

domain

Use cases and application rules, including quiz filtering

network

Retrofit configuration and API definitions

viewmodel

Screen state, user actions and coordination between the UI and data layers

ui/theme/screen

Compose screens for login, enrolment, quizzes, quiz details and profile

ui/theme/components

Reusable UI elements shared across screens

navigation

Navigation between application screens

util

Shared constants, date parsing and other helper functions

Technical approach

Several parts of the implementation focus on predictable behaviour and maintainability:

Filtering rules are placed in a dedicated use case instead of the UI or ViewModel.

Repository errors are propagated to the presentation layer so the app can distinguish an empty result from a network or server failure.

Date parsing, status colours and configuration values are centralized instead of repeated across screens.

Quiz completion is determined from submitted answers, keeping the displayed status consistent with the student's actual progress.

Lifecycle-aware refresh keeps quiz information current when the user returns to the list.

Technology

Kotlin

Jetpack Compose and Material 3

MVVM

Kotlin Coroutines and StateFlow

Retrofit

Navigation Compose

Running the project

Requirements

Android Studio

Android SDK supported by the project configuration

Internet connection for communication with the faculty service

A valid account hash

Setup

Clone the repository:

git clone REPOSITORY_URL

Open the project in Android Studio.

Allow Gradle to synchronize the project and download the required dependencies.

Select an emulator or a connected Android device.

Run the application and enter a valid account hash on the login screen.

The backend address and other shared configuration values are kept in the project's utility configuration. Verify them before running the application in a different environment.

Author

Developed by Sajra Malkić.
