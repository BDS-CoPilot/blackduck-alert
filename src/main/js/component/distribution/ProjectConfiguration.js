import React, { Component } from 'react';

import styles from '../../../css/distributionConfig.css';
import { progressIcon, missingHubData } from '../../../css/main.css';

import CheckboxInput from '../../field/input/CheckboxInput';

import {ReactBsTable, BootstrapTable, TableHeaderColumn, InsertButton, DeleteButton} from 'react-bootstrap-table';
import 'react-bootstrap-table/dist/react-bootstrap-table-all.min.css';

export default class ProjectConfiguration extends Component {
	constructor(props) {
		super(props);
	}

    createProjectList() {
        const { projects, selectedProjects } = this.props;
        let projectData = new Array();
        if(projects && projects.length > 0) {
            let rawProjects = projects;
            let missingProjects = new Array();
            for (var index in rawProjects) {
                let name = rawProjects[index]
                projectData.push({
                    name: rawProjects[index].name,
                    missing: false
                });
            }

            for(var index in selectedProjects) {
                let projectFound = projectData.find((project) => {
                    return project.name === selectedProjects[index];
                });

                if(!projectFound) {
                    projectData.push({
                        name: selectedProjects[index],
                        missing: true
                    });
                }
            }
        } else {
            let rawProjects = selectedProjects;
            for (var index in rawProjects) {
                projectData.push({
                    name: rawProjects[index],
                    missing: true
                });
            }
        }
        return projectData;
    }

    assignClassName(row, rowIdx) {
        if(row.missing) {
            return `${styles.tableRow} ${missingHubData}`;
        } else {
            return `${styles.tableRow}`;
        }
    }

	render() {
        let projectData = this.createProjectList();

		const projectTableOptions = {
	  		noDataText: 'No projects found',
	  		clearSearch: true,
            defaultSortName: 'name',
            defaultSortOrder: 'asc'
		};

		const projectsSelectRowProp = {
	  		mode: 'checkbox',
	  		clickToSelect: true,
	  		showOnlySelected: true,
            selected: this.props.selectedProjects
		};
		var progressIndicator = null;
		if (this.props.waitingForProjects) {
        	const fontAwesomeIcon = "fa fa-spinner fa-pulse fa-fw";
			progressIndicator = <div className={progressIcon}>
									<i className={fontAwesomeIcon} aria-hidden='true'></i>
								</div>;
		}

        var projectTable = null;
        if (!this.props.includeAllProjects) {
            projectTable = <div>
                                <BootstrapTable data={projectData} containerClass={styles.table} striped hover condensed selectRow={projectsSelectRowProp} search={true} options={projectTableOptions} trClassName={this.assignClassName} headerContainerClass={styles.scrollable} bodyContainerClass={styles.projectTableScrollableBody} >
                                    <TableHeaderColumn dataField='name' isKey dataSort>Project</TableHeaderColumn>
                                    <TableHeaderColumn dataField='missing' hidden>Missing Project</TableHeaderColumn>
                                </BootstrapTable>
                                {progressIndicator}
                                <p name="projectTableMessage">{this.props.projectTableMessage}</p>
                            </div>;
        }

		return (
			<div>
                <CheckboxInput labelClass={styles.fieldLabel} inputClass={styles.textInput} label="Include all projects" name="includeAllProjects" value={this.props.includeAllProjects} onChange={this.props.handleChange} errorName="includeAllProjectsError" errorValue={this.props.includeAllProjectsError}></CheckboxInput>
				{projectTable}
			</div>
		)
	}
}
