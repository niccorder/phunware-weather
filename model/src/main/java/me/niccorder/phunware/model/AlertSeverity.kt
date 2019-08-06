package me.niccorder.phunware.model

enum class AlertSeverity(val type: String) {
  ADVISORY("advisory"),
  WATCH("watch"),
  WARNING("warning"),
  UNKNOWN("unknown")
}