/**
 * \file DateImpl.cpp
 * \author Ronald T. Fernandez
 * \mail ronaldtfernandez@gmail.com
 * \version 1.0
 */

#include "DateImpl.h"
#include <sstream>
#include <iomanip>

namespace account {

connection::Connection* DateImpl::_connection = nullptr;

DateImpl::DateImpl() : _year(0), _month(0), _day(0) {
	_connection = connection::Connection::getInstance();
	_connection->activateServant(this);
};

DateImpl::DateImpl(int year, int month, int day) : _year(year), _month(month), _day(day) {
	std::ostringstream os;
	os << std::setw(2) << std::setfill('0') << this->_year << "/" << this->_month << "/" << this->_day;
	str = os.str();
	_connection = connection::Connection::getInstance();
	_connection->activateServant(this);
}

::CORBA::Long DateImpl::day() {
	return this->_day;
}
void DateImpl::day(::CORBA::Long _v) {
	this->_day = _v;
}
::CORBA::Long DateImpl::month() {
	return this->_month;
}
void DateImpl::month(::CORBA::Long _v) {
	this->_month = _v;
}
::CORBA::Long DateImpl::year() {
	return this->_year;
}
void DateImpl::year(::CORBA::Long _v) {
	this->_year = _v;
}

char* DateImpl::toString() {
	return const_cast<char*>(str.c_str());
};

} /* namespace account */