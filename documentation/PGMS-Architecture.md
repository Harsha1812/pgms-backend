PGMS – PG Management System
Multi-Tenant SaaS Architecture Documentation

Version: 1.0

1. System Overview

PGMS is a multi-tenant SaaS platform for managing PG hostels with:

Multi-owner architecture

Branch-level operations

GST-compliant billing

Discount & referral engine

Tenant portal

Admin dashboard

Platform subscription monetization

2. Architecture Principles

Shared DB, shared schema, logical isolation via owner_id

Immutable financial records

Snapshot-based pricing and tax storage

Event-driven notifications

Strict separation between:

Tenant billing (Owner revenue)

Platform billing (SaaS revenue)

3. Security Domains
   Domain	Role	Scope
   Platform	SUPER_ADMIN	All owners
   Business	OWNER_ADMIN	All branches
   Business	BRANCH_MANAGER	Single branch
   Business	STAFF	Operational only
   Tenant	TENANT	Self-only access

JWT claims include ownerId / branchId / tenantId accordingly.

4. Core Modules
   4.1 Identity & Structure

owners

businesses

branches

users

4.2 Inventory

rooms

beds

room_pricing

4.3 Tenant Lifecycle

tenants

bed_allocations

deposits

4.4 Billing Engine

monthly_invoices

payments

billing calculator

4.5 Financial Enhancements

GST inclusive rent

GST exclusive late fee

Upfront discount

Referral rewards

4.6 Notifications

Email

PDF invoice

Event-based trigger model

4.7 Admin Dashboard

Occupancy metrics

Revenue metrics

Pending payments

4.8 Tenant Portal

Invoice history

Download PDF

Referral tracking

4.9 Platform Monetization

billing_plans

owner_subscriptions

owner_usage_snapshots

platform_invoices

platform_payments

5. Billing Logic Summary
   Rent

GST inclusive.

Late Fee

GST exclusive.

Discount Order

Extract base

Apply upfront discount

Apply referral discount

Recalculate GST

Add late fee + GST

6. Scheduled Jobs

Monthly Invoice Generation

Daily Late Fee Job

Trial Expiry Job

Subscription Billing Job

7. Integration

Stripe / Razorpay

UPI QR

Webhook verification

Auto-recurring subscription