package io.github.tavstaldev.nexus.config;

import io.github.tavstaldev.nexus.config.reporting.Report;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashSet;
import java.util.Set;

@ConfigSerializable
public class ReportData {
    private Set<Report> reports;

    public ReportData() {
        this.reports = new HashSet<>();
    }

    public ReportData(Set<Report> reports) {
        this.reports = reports;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void add(Report report) {
        this.reports.add(report);
    }

    public void remove(Report report) {
        this.reports.remove(report);
    }
}
