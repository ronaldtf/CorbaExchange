/**
 * @file OperationImpl.cpp
 * @author Ronald T. Fernandez
 * @version 1.0
 */

#include "OperationImpl.h"

namespace account {

std::shared_ptr<connection::Connection> OperationImpl::_connection = nullptr;

OperationImpl::~OperationImpl() {
	_connection->deactivateServant(this);
}

OperationImpl::OperationImpl(corbaAccount::Operation_ptr op) : _type(op->type()), _amount(op->amount()), _operationId(op->operationId()) {
	// Activate the CORBA object
	_connection = connection::Connection::getInstance();
	_connection->activateServant(this);
}

OperationImpl::OperationImpl(corbaAccount::operationType type, const float amount, const int operationId) : _type(type), _amount(amount), _operationId(operationId) {
	// Activate the CORBA object
	_connection = connection::Connection::getInstance();
	_connection->activateServant(this);
}

corbaAccount::operationType OperationImpl::type() {
	return _type;
}

void OperationImpl::type(::corbaAccount::operationType _v) {
	this->_type = _v;
}

::CORBA::Float OperationImpl::amount() {
	return _amount;
}

void OperationImpl::amount(::CORBA::Float _v) {
	this->_amount = _v;
}

::CORBA::Long OperationImpl::operationId() {
	return _operationId;
}

void OperationImpl::operationId(::CORBA::Long _v) {
	_operationId = _v;
}

} /* namespace account */
