# 🚀 Redis Rate Limiting Demo Application

Welcome to the **Redis Rate Limiting Demo**! This is a simple yet powerful application built with **Spring Boot** and **Redis** that demonstrates how you can easily implement **rate limiting** to protect your APIs from abuse and overuse. 

With this demo, you’ll see how Redis helps in tracking and controlling the number of requests per user, and how to efficiently handle **rate-limiting exceptions** using **HTTP 429 Too Many Requests**.

## 💡 Key Features

- **Rate Limiting**: Limit the number of requests users can make within a specified time window (e.g., 5 requests per minute).
- **Redis Integration**: Uses Redis to track request counts for each user and reset them after the time window expires.
- **White-Listing**: Easily bypass rate limits for certain users.
- **Custom Error Handling**: Returns a clean, informative error message with a **429 HTTP Status** when users exceed the rate limit.
- **Minimal Setup**: All you need is Docker, Spring Boot, and Redis. A couple of commands, and you’re up and running!

---

## 🛠 Prerequisites

To get started, you’ll need the following:

- **Docker** (for running Redis in a container)
- **Java 11+** (to run the Spring Boot application)
- **Maven** or **Gradle** (to build the project)
- **Redis** (we’ll use Docker to run it!)

---

## 🚀 Getting Started

### 1. Clone the Repository

Start by cloning this repository to your local machine:

```bash
git clone https://github.com/yourusername/redis-rate-limiting-demo.git
cd redis-rate-limiting-demo
