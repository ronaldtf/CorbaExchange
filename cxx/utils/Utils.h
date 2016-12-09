/**
 * \file Utils.hpp
 * \author Ronald T. Fernandez
 * \mail ronaldtfernandez@gmail.com
 * \version 1.0
 */

#include <map>
#include <vector>
#include "../idl/Account.hh"

namespace utils {
	class Utils {
	public:

		enum class OperationType {ADD=1, WITHDRAW=2};

		static void trim(std::string &s);

		static std::vector<std::string> tokenize(const std::string &s);

		static void parseFile(const std::string fileName, std::map<std::string, std::string> &properties);

		static corbaAccount::operationType convertType(OperationType opType);

		static Utils::OperationType convertType(corbaAccount::operationType opType);

	};
}